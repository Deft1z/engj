package com.kge.energy.crm.order.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.common.constans.ConstParam;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.external.wechat.applet.property.WeChatAppletProperties;
import com.kge.energy.crm.external.wechat.applet.req.SendSubscribeReq;
import com.kge.energy.crm.external.wechat.applet.req.contract.ContractFinishMsgReq;
import com.kge.energy.crm.external.wechat.applet.req.contract.ContractFinishValueReq;
import com.kge.energy.crm.external.wechat.applet.service.WeChatAppletInfraService;
import com.kge.energy.crm.order.req.ContractReq;
import com.kge.energy.crm.order.req.WxUserWorkOrderReq;
import com.kge.energy.crm.order.req.contract.CreateContractReq;
import com.kge.energy.crm.order.req.contract.UpdateProjectTimeReq;
import com.kge.energy.crm.order.resp.ContractResp;
import com.kge.energy.crm.repository.dao.BUserDao;
import com.kge.energy.crm.repository.dao.ScServiceContractDao;
import com.kge.energy.crm.repository.dao.WfFormDao;
import com.kge.energy.crm.repository.dao.WfFormFlowDao;
import com.kge.energy.crm.repository.entity.BUser;
import com.kge.energy.crm.repository.entity.ScServiceContract;
import com.kge.energy.crm.repository.entity.WfForm;
import com.kge.energy.crm.repository.entity.WfFormFlow;
import com.kge.energy.crm.repository.entityext.param.WxUserWorkOrderParam;
import com.kge.energy.crm.repository.entityext.result.ContractResult;
import com.kge.platform.framework.common.exception.ServiceException;
import com.kge.platform.framework.common.net.CommonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.kge.energy.crm.enums.RoleEnums.JT_CUSTOMER;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ContractService {

    private final BUserDao bUserDao;
    private final WfFormDao wfFormDao;
    private final WfFormFlowDao wfFormFlowDao;
    private final ScServiceContractDao scServiceContractDao;

    private final WeChatAppletProperties weChatAppletProperties;
    private final WeChatAppletInfraService weChatAppletInfraService;

    /**
     * 获取合同
     */
    public List<ContractResp> form(ContractReq req) {

        List<ContractResult> resultList = scServiceContractDao.form(req.getFormId());

        return BeanUtil.copyToList(resultList, ContractResp.class);
    }

    /**
     * 微信客户小程序 -> 获取合同
     */
    public PageResp<ContractResult> contractPageByUserIdLoad(WxUserWorkOrderReq req) {
        IPage<WxUserWorkOrderParam> reqIpage = new Page<>(req.getCurrentPage(), req.getPageSize());
        WxUserWorkOrderParam wxUserWorkOrderParam = BeanUtil.copyProperties(req, WxUserWorkOrderParam.class);
        System.out.println("wxUserWorkOrderParam = " + wxUserWorkOrderParam);
        IPage<ContractResult> pages = scServiceContractDao.contractPageByUserIdLoad(reqIpage, wxUserWorkOrderParam);
        List<ContractResult> resps = BeanUtil.copyToList(pages.getRecords(), ContractResult.class);
        return new PageResp<ContractResult>()
                .setList(resps)
                .setCurrentPage(pages.getCurrent())
                .setPageSize(pages.getSize())
                .setTotal(pages.getTotal());

    }

    @Transactional
    public CommonResult<Object> contractAdd(CreateContractReq req) {
        LocalDateTime now = LocalDateTime.now();

        LambdaQueryWrapper<ScServiceContract> queryWrapper = Wrappers.<ScServiceContract>lambdaQuery()
                .eq(ScServiceContract::getCode, req.getCode());
        long count = scServiceContractDao.count(queryWrapper);
        if (count > 0) {
            return CommonResult.suc("contract_code_repeat");
        }

        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();

        //新增合同
        ScServiceContract contract = BeanUtil.copyProperties(req, ScServiceContract.class);
        Opt.ofBlankAble(req.getAmount()).ifPresent(a -> contract.setAmount(NumberUtil.round(Double.parseDouble(a), 2).doubleValue()));
        Opt.ofBlankAble(req.getSigningTime()).ifPresent(s -> contract.setSigningTime(LocalDateTimeUtil.parse(s, DatePattern.NORM_DATE_PATTERN)));
        contract.setStatus(ConstParam.ContractNotBegin);
        contract.setFlag(1);
        contract.setCreateUserId(userInfoDto.getUserId().intValue());
        contract.setServiceUnit(userInfoDto.getRoleCodes().contains(JT_CUSTOMER.getCode()) ?
                req.getServiceUnit() : userInfoDto.getOrganizationList().get(0).getId());
        contract.setTenantId(userInfoDto.getTenantId());
        scServiceContractDao.save(contract);

        //更新form
        WfForm form = wfFormDao.getById(req.getFormId());
        form.setStatus(ConstParam.Processed)
                .setSubStatus(ConstParam.Processed)
                .setModifyUserId(userInfoDto.getUserId().intValue())
                .setTimeFinished(now)
                .setCurrentOrgId(userInfoDto.getOrganizationList().get(0).getId())
                .setCurrentRoleId(userInfoDto.getRoleList().get(0).getId());
        wfFormDao.updateById(form);

        //新增流程节点
        WfFormFlow wfFormFlow = new WfFormFlow().setFormId(req.getFormId())
                .setUserId(userInfoDto.getUserId().intValue())
                .setTimeAction(now)
                .setActionType(ConstParam.FlowCompanyContract)
                .setActionContent(req.getContent())
                .setStatus(ConstParam.FlowCompanyContract)
                .setTenantId(userInfoDto.getTenantId());
        wfFormFlowDao.save(wfFormFlow);

        return CommonResult.suc(true);
    }

    public CommonResult<Object> projectTimeEdit(UpdateProjectTimeReq req) {
        return switch (req.getMode()) {

            //开工时间
            case 0 -> updateStartTime(req);

            //竣工时间
            case 1 -> updateFinishTime(req);

            default -> throw new ServiceException("参数验证失败");
        };
    }

    @Transactional
    protected CommonResult<Object> updateStartTime(UpdateProjectTimeReq req) {
        if (StrUtil.isBlank(req.getProjectTime())) {
            return CommonResult.fail("参数验证失败");
        }

        LambdaUpdateWrapper<ScServiceContract> updateWrapper = Wrappers.<ScServiceContract>lambdaUpdate()
                .set(ScServiceContract::getProjectStartTime, LocalDateTimeUtil.parse(req.getProjectTime(), DatePattern.NORM_DATE_PATTERN))
                .set(ScServiceContract::getStatus, ConstParam.ContractUnderWay)
                .eq(ScServiceContract::getServiceContractId, req.getServiceContractId());
        scServiceContractDao.update(updateWrapper);

        return CommonResult.suc(true);
    }

    @Transactional
    protected CommonResult<Object> updateFinishTime(UpdateProjectTimeReq req) {
        LocalDateTime now = LocalDateTime.now();

        if (StrUtil.isBlank(req.getProjectTime())) {
            throw new ServiceException("参数验证失败");
        }

        ScServiceContract scServiceContract = scServiceContractDao.getById(req.getServiceContractId());
        scServiceContract.setProjectEndTime(LocalDateTimeUtil.parse(req.getProjectTime(), DatePattern.NORM_DATE_PATTERN));
        scServiceContractDao.updateById(scServiceContract);

        LocalDateTime projectEndTime = LocalDateTimeUtil.parse(req.getProjectTime(), DatePattern.NORM_DATE_PATTERN);
        if (!projectEndTime.isAfter(now)) {
            BUser bUser = bUserDao.findUserByContractId(req.getServiceContractId());
            if (ObjectUtil.isNotNull(bUser)) {
                scServiceContract.setStatus(ConstParam.RemainToBeEvaluated);
                boolean updateResult = scServiceContractDao.updateById(scServiceContract);
                if (updateResult) {
                    ContractFinishMsgReq contractFinishMsgReq = new ContractFinishMsgReq()
                            .setName(new ContractFinishValueReq().setValue(scServiceContract.getName()))
                            .setRemark(new ContractFinishValueReq().setValue(scServiceContract.getRemark()));
                    SendSubscribeReq<ContractFinishMsgReq> sendSubscribeReq = new SendSubscribeReq<ContractFinishMsgReq>()
                            .setTemplateId(weChatAppletProperties.getContractFinishTemplate())
                            .setPage(weChatAppletProperties.getContractFinishTemplate())
                            .setToUserOpenId(bUser.getOpenId())
                            .setData(contractFinishMsgReq);
                    weChatAppletInfraService.sendSubscribe(sendSubscribeReq);
                }
            }
        }

        return CommonResult.suc(true);
    }
}
