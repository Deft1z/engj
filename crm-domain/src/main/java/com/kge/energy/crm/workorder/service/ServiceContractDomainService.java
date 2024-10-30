package com.kge.energy.crm.workorder.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kge.energy.crm.common.constans.ConstParam;
import com.kge.energy.crm.common.dto.BizOrderFromContentDto;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.util.AppletLinkUtils;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.BizFunctionEnums;
import com.kge.energy.crm.enums.DataPermissionRangeTypeEnums;
import com.kge.energy.crm.enums.RoleEnums;
import com.kge.energy.crm.external.wechat.applet.property.WeChatAppletProperties;
import com.kge.energy.crm.external.wechat.applet.req.SendSubscribeReq;
import com.kge.energy.crm.external.wechat.applet.req.contract.ContractFinishMsgReq;
import com.kge.energy.crm.external.wechat.applet.req.contract.ContractFinishValueReq;
import com.kge.energy.crm.external.wechat.applet.service.WeChatAppletInfraService;
import com.kge.energy.crm.msg.MsgDomainService;
import com.kge.energy.crm.permission.service.DataPermissionDomainService;
import com.kge.energy.crm.repository.dao.*;
import com.kge.energy.crm.repository.entity.BUser;
import com.kge.energy.crm.repository.entity.ScServiceContract;
import com.kge.energy.crm.repository.entity.WfForm;
import com.kge.energy.crm.repository.entity.WfFormFlow;
import com.kge.energy.crm.repository.entityext.result.ContractResult;
import com.kge.energy.crm.user.service.UserDomainService;
import com.kge.energy.crm.workorder.req.ServiceContractAddReq;
import com.kge.energy.crm.workorder.req.ServiceContractDetailReq;
import com.kge.energy.crm.workorder.req.ServiceContractReq;
import com.kge.energy.crm.workorder.req.ServiceContractUpdateProjectTimeReq;
import com.kge.energy.crm.workorder.resp.ServiceContractResp;
import com.kge.energy.msg.param.ContractAddMsgToUserParam;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceContractDomainService {

    private final BUserDao bUserDao;
    private final WfFormDao wfFormDao;
    private final WfFormFlowDao wfFormFlowDao;
    private final ScServiceContractDao scServiceContractDao;
    private final WeChatAppletProperties weChatAppletProperties;
    private final WeChatAppletInfraService weChatAppletInfraService;
    private final DataPermissionDomainService dataPermissionDomainService;
    private final BOrganizationDao bOrganizationDao;
    private final UserDomainService userDomainService;
    private final MsgDomainService msgDomainService;

    public List<ServiceContractResp> getServiceContractList(ServiceContractReq req) {
        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();
        DataPermissionRangeTypeEnums dataEnums = dataPermissionDomainService.getCurrentUserDataPermission(BizFunctionEnums.CONTRACT_LIST);

        List<ContractResult> resultList = scServiceContractDao.form(req.getFormId(), userInfoDto, dataEnums);
        return BeanUtil.copyToList(resultList, ServiceContractResp.class);
    }

    public ServiceContractResp getContractDetailByContractId(ServiceContractDetailReq req) {
        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();
        DataPermissionRangeTypeEnums dataEnums = dataPermissionDomainService.getCurrentUserDataPermission(BizFunctionEnums.CONTRACT_LIST);
        ContractResult contractResult = scServiceContractDao.getContractDetailByContractId(req.getContractId(), userInfoDto, dataEnums);
        return BeanUtil.copyProperties(contractResult, ServiceContractResp.class);
    }

    public Boolean addServiceContract(ServiceContractAddReq req) {
        LocalDateTime now = LocalDateTime.now();
        //校验合同code是否存在
        LambdaQueryWrapper<ScServiceContract> queryWrapper = Wrappers.<ScServiceContract>lambdaQuery()
                .eq(ScServiceContract::getCode, req.getCode());
        ScServiceContract contract = scServiceContractDao.getOne(queryWrapper);
        if (contract != null) {
            log.info("==> 合同已存在，不重复添加！");
            throw new ServiceException("合同编号已存在，不重复添加！");
        }

        UserInfoDto operator = UserInfoContextUtils.getCurrentUserInfo();
        ScServiceContract scServiceContract = new ScServiceContract()
                .setFormId(req.getFormId())
                .setName(req.getName())
                .setCode(req.getCode())
                .setAmount(req.getAmount())
                .setProjectCode(req.getProjectCode())
                .setSigningTime(req.getSigningTime().atStartOfDay())
                .setServiceUnit(operator.getOrganizationList().iterator().next().getId())
                .setStatus(ConstParam.ContractNotBegin)
                .setFlag(1)
                .setCreateUserId(operator.getUserId().intValue())
                .setRemark(req.getRemark())
                .setTenantId(operator.getTenantId());
        if (operator.getRoleCodes().contains(RoleEnums.JT_CUSTOMER.getCode())) {
            scServiceContract.setServiceUnit(req.getServiceUnit());
        }
        scServiceContractDao.save(scServiceContract);

        //变更工单信息
        WfForm wfForm = wfFormDao.getById(req.getFormId());
        Integer currentOrgId = wfForm.getCurrentOrgId();
        LambdaUpdateWrapper<WfForm> wfUpdateWrapper = Wrappers.<WfForm>update().lambda()
                .set(WfForm::getStatus, ConstParam.Processed)
                .set(WfForm::getSubStatus, ConstParam.Processed)
                .set(WfForm::getModifyUserId, operator.getUserId())
                .set(WfForm::getTimeFinished, now)
                .set(WfForm::getCurrentOrgId, operator.getOrganizationList().iterator().next().getId())
                .set(WfForm::getCurrentRoleId, operator.getRoleList().iterator().next().getId())
                .eq(WfForm::getFormId, req.getFormId());
        wfFormDao.update(wfUpdateWrapper);

        //新增工单流转添加合同记录
        WfFormFlow wfFormFlow = new WfFormFlow()
                .setFormId(req.getFormId())
                .setUserId(operator.getUserId().intValue())
                .setCreateUserId(operator.getUserId().intValue())
                .setTimeAction(now)
                .setActionType(ConstParam.FlowCompanyContract)
                .setActionContent(req.getRemark())
                .setStatus(ConstParam.FlowCompanyContract)
                .setTenantId(operator.getTenantId())
                .setServiceUnitId(operator.getOrganizationList().iterator().next().getId());
        wfFormFlowDao.save(wfFormFlow);

        //发送微信小程序消息，通知客户
        BizOrderFromContentDto fromContent = JSONUtil.toBean(wfForm.getContent(), BizOrderFromContentDto.class);
        ContractAddMsgToUserParam msgParam = new ContractAddMsgToUserParam();
        List<RoleEnums> roleEnums = dataPermissionDomainService.getFunctionRoleEnums(operator.getTenantId(), msgParam.getFunctionCode());
        if (!roleEnums.isEmpty()) {
            msgParam.setOrderName(fromContent.getBusinessName());
            msgParam.setOrderCode(fromContent.getCode());
            msgParam.setServiceUnit(bOrganizationDao.getById(currentOrgId).getName());
            msgParam.setServicePerson(bUserDao.getById(operator.getUserId()).getRealname());
            msgParam.setStatus(ConstParam.FlowCompanyContract);
            msgParam.setAddTime(now.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
            msgParam.setPathUrl(weChatAppletInfraService.getWeChatAppletUrlLink(null, AppletLinkUtils.getContractDetailQuery(scServiceContract.getServiceContractId()), 30));
            msgParam.setTenantId(operator.getTenantId());
            msgParam.setMsgBizId(scServiceContract.getServiceContractId());

            msgDomainService.sendCrmMsg(msgParam.setNotifyUsers(userDomainService.getUserContact(roleEnums, operator.getTenantId())));

            if (roleEnums.stream().map(RoleEnums::getCode).toList().contains(RoleEnums.APPLET_USER.getCode())) {
                msgDomainService.sendCrmMsg(msgParam.setNotifyUsers(userDomainService.getUserContact(wfForm.getCreateUserId(), operator.getTenantId())));
            }
        }

        return true;
    }

    @Transactional
    public Boolean updateProjectTime(ServiceContractUpdateProjectTimeReq req) {
        return switch (req.getMode()) {
            case 0 -> updateStartTime(req);
            case 1 -> updateFinishTime(req);
            default -> throw new ServiceException("参数错误！");
        };
    }

    private Boolean updateStartTime(ServiceContractUpdateProjectTimeReq req) {

        ScServiceContract contract = scServiceContractDao.getById(req.getServiceContractId());
        if (contract == null) {
            throw new ServiceException("合同不存在!");
        }

        LocalDateTime projectStartTime = LocalDateTimeUtil.parse(req.getProjectTime(), DatePattern.NORM_DATE_PATTERN);
        if (contract.getSigningTime().isAfter(projectStartTime)) {
            throw new ServiceException("项目开始时间不能早于合同签订时间!");
        }

        contract.setStatus(ConstParam.ContractUnderWay)
                .setProjectStartTime(projectStartTime);

        return scServiceContractDao.updateById(contract);
    }

    private Boolean updateFinishTime(ServiceContractUpdateProjectTimeReq req) {

        ScServiceContract contract = scServiceContractDao.getById(req.getServiceContractId());
        if (contract == null) {
            throw new ServiceException("合同不存在!");
        }

        LocalDateTime projectFinishTime = LocalDateTimeUtil.parse(req.getProjectTime(), DatePattern.NORM_DATE_PATTERN);
        if (contract.getProjectStartTime().isAfter(projectFinishTime)) {
            throw new ServiceException("合同竣工时间不能早于开始时间!");
        }

        LocalDateTime todayLocalDateTime = LocalDateTimeUtil.parse(DateUtil.today(), DatePattern.NORM_DATE_PATTERN);
        if (projectFinishTime.isAfter(todayLocalDateTime)) {
            throw new ServiceException("合同竣工时间不能晚于当前时间!");
        }

        contract.setStatus(ConstParam.RemainToBeEvaluated)
                .setProjectEndTime(projectFinishTime);

        boolean updateResult = scServiceContractDao.updateById(contract);
        if (updateResult) {
            sendServiceContractUpdateMsg(contract);
        }
        return updateResult;
    }

    private void sendServiceContractUpdateMsg(ScServiceContract scServiceContract) {
        CompletableFuture.runAsync(() -> {
            try {
                BUser bUser = bUserDao.findUserByContractId(scServiceContract.getServiceContractId());
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
            } catch (Exception e) {
                log.error("==> 发送服务合同id={}更新消息异常：{}", scServiceContract.getServiceContractId(), e.getMessage());
            }
        });
    }
}
