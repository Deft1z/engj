package com.kge.energy.crm.contract.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.common.constans.ConstParam;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.RedisLockUtils;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.contract.req.ScServiceContractEvaAddReq;
import com.kge.energy.crm.contract.req.ScServiceContractPageReq;
import com.kge.energy.crm.contract.req.ScServiceContractProjEndTimeUpdReq;
import com.kge.energy.crm.contract.resp.ScServiceContractResp;
import com.kge.energy.crm.enums.BizFunctionEnums;
import com.kge.energy.crm.enums.DataPermissionRangeTypeEnums;
import com.kge.energy.crm.enums.RoleEnums;
import com.kge.energy.crm.msg.MsgDomainService;
import com.kge.energy.crm.permission.service.DataPermissionDomainService;
import com.kge.energy.crm.repository.dao.ScContractEvaluateDao;
import com.kge.energy.crm.repository.dao.ScServiceContractDao;
import com.kge.energy.crm.repository.entity.ScContractEvaluate;
import com.kge.energy.crm.repository.entity.ScServiceContract;
import com.kge.energy.crm.repository.entityext.param.WxUserWorkOrderParam;
import com.kge.energy.crm.repository.entityext.result.ContractResult;
import com.kge.energy.crm.user.service.UserDomainService;
import com.kge.energy.msg.dto.UserContactDto;
import com.kge.energy.msg.param.ContractEvaluateMsgToRoleParam;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ScServiceContractService {

    @Value("${spring.data.redis.front}")
    private String redisFront;

    private final ScServiceContractDao scServiceContractDao;

    private final ScContractEvaluateDao scContractEvaluateDao;

    private final RedisLockUtils redisLockUtils;

    private final DataPermissionDomainService dataPermissionDomainService;

    private final UserDomainService userDomainService;

    private final MsgDomainService msgDomainService;

    /**
     * 获取服务合同列表
     *
     * @return
     */
    public PageResp<ScServiceContractResp> getPage(ScServiceContractPageReq req) {
        IPage<WxUserWorkOrderParam> reqIpage = new Page<>(req.getCurrentPage(), req.getPageSize());
        WxUserWorkOrderParam wxUserWorkOrderParam = BeanUtil.copyProperties(req, WxUserWorkOrderParam.class);
        log.info("==> wxUserWorkOrderParam = {}", wxUserWorkOrderParam);
        //设置userId、roleId
        UserInfoDto currentUserInfo = UserInfoContextUtils.getCurrentUserInfo();
        wxUserWorkOrderParam.setUserId(currentUserInfo.getUserId().intValue());
        wxUserWorkOrderParam.setTenantId(currentUserInfo.getTenantId());
        //超管super_admin和集团客服jt_customer，可查看全部服务合同
        //二级公司客服sub_company_customer，仅可查看自己创建的服务合同
        wxUserWorkOrderParam.setRoleCodes(currentUserInfo.getRoleCodes());

        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();
        DataPermissionRangeTypeEnums dataEnums = dataPermissionDomainService.getCurrentUserDataPermission(BizFunctionEnums.CONTRACT_LIST);

        IPage<ContractResult> pages = scServiceContractDao.getPage(reqIpage, wxUserWorkOrderParam, userInfoDto, dataEnums);
        List<ScServiceContractResp> resps = BeanUtil.copyToList(pages.getRecords(), ScServiceContractResp.class);
        return new PageResp<ScServiceContractResp>()
                .setList(resps)
                .setCurrentPage(pages.getCurrent())
                .setPageSize(pages.getSize())
                .setTotal(pages.getTotal());
    }


    @Transactional
    public Boolean update(ScServiceContractProjEndTimeUpdReq req) {
        Integer contractId = req.getServiceContractId();
        String lockKey = redisFront + "contract_" + contractId;
        String requestId = IdUtil.fastSimpleUUID();
        try {
            boolean locked = redisLockUtils.lock(lockKey, requestId, 60L);
            if (!locked) {
                throw new ServiceException("合同已锁定，请勿同时操作!");
            }
            LambdaQueryWrapper<ScServiceContract> queryWrapper = Wrappers.<ScServiceContract>lambdaQuery()
                    .eq(ScServiceContract::getServiceContractId, req.getServiceContractId());
            ScServiceContract contract = scServiceContractDao.getOne(queryWrapper);
            if (contract == null) {
                throw new ServiceException("合同不存在!");
            }
            //数据时间校验
            if (contract.getProjectStartTime().isAfter(req.getProjectEndTime())) {
                throw new ServiceException("项目开始时间不能早于项目结束时间!");
            }

            LambdaUpdateWrapper<ScServiceContract> updateWrapper = Wrappers.<ScServiceContract>update().lambda()
                    .set(ScServiceContract::getProjectEndTime, req.getProjectEndTime())
                    .set(ScServiceContract::getStatus, ConstParam.RemainToBeEvaluated)
                    .eq(ScServiceContract::getServiceContractId, req.getServiceContractId());
            return scServiceContractDao.update(updateWrapper);
        } finally {
            redisLockUtils.unlock(lockKey, requestId);
        }
    }


    @Transactional
    public Boolean addEvaluation(ScServiceContractEvaAddReq req) {

        ScServiceContract contract = scServiceContractDao.getById(req.getServiceContractId());
        if (contract == null) {
            throw new ServiceException("合同不存在!");
        }

        LocalDateTime todayLocalDateTime = LocalDateTimeUtil.parse(DateUtil.today(), DatePattern.NORM_DATE_PATTERN);
        if (todayLocalDateTime.isBefore(contract.getProjectEndTime())) {
            throw new ServiceException("未达到合同竣工时间，不能评价!");
        }

        //获取当前登录操作用户信息
        UserInfoDto operator = UserInfoContextUtils.getCurrentUserInfo();
        //新增评价
        ScContractEvaluate scContractEvaluate = new ScContractEvaluate()
                .setServiceContractId(req.getServiceContractId())
                .setEvaluate(req.getEvaluate())
                .setSatisfaction(req.getSatisfaction())
                .setTenantId(UserInfoContextUtils.getCurrentTenantId());
        boolean saved = scContractEvaluateDao.save(scContractEvaluate);
        //更新合同状态
        LambdaUpdateWrapper<ScServiceContract> updateWrapper = Wrappers.<ScServiceContract>update().lambda()
                .set(ScServiceContract::getStatus, ConstParam.HasEvaluated)
                .eq(ScServiceContract::getServiceContractId, req.getServiceContractId());
        boolean updated = scServiceContractDao.update(updateWrapper);

        //发送消息通知集团客服
        sendMsg(req, operator, contract);

        return saved && updated;
    }

    private void sendMsg(ScServiceContractEvaAddReq req, UserInfoDto operator, ScServiceContract contract) {
        ContractEvaluateMsgToRoleParam msgParam = new ContractEvaluateMsgToRoleParam();
        List<RoleEnums> roleEnums = dataPermissionDomainService.getFunctionRoleEnums(operator.getTenantId(), msgParam.getFunctionCode());
        if (!roleEnums.isEmpty()) {
            List<UserContactDto> userContact = userDomainService.getUserContact(roleEnums, operator.getTenantId());
            msgDomainService.sendCrmMsg(msgParam.setContractCode(contract.getCode())
                    .setContractName(contract.getName())
                    .setSignedTime(contract.getSigningTime() != null ? contract.getSigningTime().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)) : "")
                    .setStartTime(contract.getProjectStartTime() != null ? contract.getProjectStartTime().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)) : "")
                    .setEndTime(contract.getProjectEndTime() != null ? contract.getProjectEndTime().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)) : "")
                    .setSatisfaction(req.getSatisfaction().toString())
                    .setEvaluate(req.getEvaluate())
                    .setTenantId(operator.getTenantId())
                    .setNotifyUsers(userContact)
                    .setPathUrl(null)
                    .setMsgBizId(contract.getServiceContractId())
            );
        }
    }

}
