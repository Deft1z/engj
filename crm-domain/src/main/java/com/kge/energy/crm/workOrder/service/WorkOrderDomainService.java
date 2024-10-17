package com.kge.energy.crm.workOrder.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.*;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.common.button.enums.WorkOrderButtonEnum;
import com.kge.energy.crm.common.button.helper.ConsultingButtonHelper;
import com.kge.energy.crm.common.button.resp.BaseButton;
import com.kge.energy.crm.common.constans.ConstParam;
import com.kge.energy.crm.common.dto.BizOrderFromContentDto;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.AuthVerifyUtils;
import com.kge.energy.crm.common.util.RedisLockUtils;
import com.kge.energy.crm.common.util.RedisUtils;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.BizFunctionEnums;
import com.kge.energy.crm.enums.DataPermissionRangeTypeEnums;
import com.kge.energy.crm.enums.RoleEnums;
import com.kge.energy.crm.external.wechat.applet.service.WeChatAppletInfraService;
import com.kge.energy.crm.msg.MsgDomainService;
import com.kge.energy.crm.permission.service.DataPermissionDomainService;
import com.kge.energy.crm.repository.dao.*;
import com.kge.energy.crm.repository.entity.ScServiceContract;
import com.kge.energy.crm.repository.entity.WfForm;
import com.kge.energy.crm.repository.entity.WfFormFlow;
import com.kge.energy.crm.repository.entityext.param.WorkOrderListParam;
import com.kge.energy.crm.repository.entityext.result.*;
import com.kge.energy.crm.user.service.UserDomainService;
import com.kge.energy.crm.workOrder.req.WfFormFlowReq;
import com.kge.energy.crm.workOrder.req.WfFormPageReq;
import com.kge.energy.crm.workOrder.req.WorkOrderAddReq;
import com.kge.energy.crm.workOrder.req.WorkOrderUpdateReq;
import com.kge.energy.crm.workOrder.resp.FormWithdrawReturnResp;
import com.kge.energy.crm.workOrder.resp.WfFormFlowListResp;
import com.kge.energy.crm.workOrder.resp.WfFormFlowResp;
import com.kge.energy.crm.workOrder.resp.WfFormPageResp;
import com.kge.energy.crm.workOrder.util.WorkFlowCommentUtil;
import com.kge.energy.msg.dto.UserContactDto;
import com.kge.energy.msg.param.*;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 业务工单公共service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkOrderDomainService {

    private static final String WORK_CODE_CACHE_KEY_PREFIX = "crm_order_code:";

    @Value("${spring.data.redis.front}")
    private String redisFront;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    private final WfFormDao wfFormDao;
    private final WfFormFlowDao wfFormFlowDao;
    private final ScServiceContractDao scServiceContractDao;
    private final BUserDao bUserDao;
    private final BRoleDao bRoleDao;
    private final BOrganizationDao bOrganizationDao;
    private final RedisUtils redisUtils;
    private final RedisLockUtils redisLockUtils;
    private final DataPermissionDomainService dataPermissionDomainService;
    private final UserDomainService userDomainService;
    private final MsgDomainService msgDomainService;
    private final WeChatAppletInfraService weChatAppletInfraService;

    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean addWorkOrder(WorkOrderAddReq req) {
        LocalDateTime now = LocalDateTime.now();
        //生成工单编号
        String code = genOrderCode();

        WorkOrderAddReq.WorkOrderContent content = req.getContent();
        content.setCode(code);
        //参数校验
        if (!PhoneUtil.isPhone(content.getMobile())) {
            throw new ServiceException("手机号码不正确");
        }

        //登录用户信息
        UserInfoDto operator = UserInfoContextUtils.getCurrentUserInfo();

        //保存工单信息
        Integer currentRoleId = bRoleDao.getRoleIdByCode(RoleEnums.JT_CUSTOMER.getCode(), operator.getTenantId());
        WfForm wfForm = new WfForm();
        wfForm.setFormTypeId(1);
        wfForm.setContent(JSONUtil.toJsonStr(content));
        wfForm.setStatus(ConstParam.WaitingForProcessing);
        wfForm.setSubStatus(ConstParam.WaitingForProcessing);
        wfForm.setTimeSubmit(now);
        wfForm.setFlag(1);
        wfForm.setCreateUserId(operator.getUserId().intValue());
        wfForm.setRemark(req.getRemark());
        //集团总部
        wfForm.setCurrentOrgId(1);
        //集团客服
        wfForm.setCurrentRoleId(currentRoleId);
        //租户
        wfForm.setTenantId(operator.getTenantId());
        wfFormDao.save(wfForm);

        //保存流转记录
        WfFormFlow wfFormFlow = new WfFormFlow();
        wfFormFlow.setFormId(wfForm.getFormId());
        wfFormFlow.setTimeAction(now);
        wfFormFlow.setActionType(ConstParam.FlowStart);
        wfFormFlow.setStatus(ConstParam.FlowStart);
        wfFormFlow.setSubStatus(ConstParam.FlowTagGroup);
        wfFormFlow.setCreateUserId(operator.getUserId().intValue());
        wfFormFlow.setTenantId(operator.getTenantId());
        wfFormFlow.setServiceUnitId(1);
        wfFormFlowDao.save(wfFormFlow);

        //发送消息，通知集团客服
        BizOrderCreateMsgToRoleParam msgParam = new BizOrderCreateMsgToRoleParam();
        List<RoleEnums> roleEnums = dataPermissionDomainService.getFunctionRoleEnums(operator.getTenantId(), msgParam.getFunctionCode());
        if (!roleEnums.isEmpty()) {
            List<UserContactDto> userContact = userDomainService.getUserContact(roleEnums, operator.getTenantId());
            msgParam.setOrderName(content.getBusinessName());
            msgParam.setArea(content.getArea());
            msgParam.setElectricityCapacity(content.getElectricityCapacity());
            msgParam.setOrderCode(content.getCode());
            msgParam.setCreateTime(now.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
            msgParam.setCustomerName(content.getCustomerName());
            msgParam.setMobile(content.getMobile());
            msgParam.setRemark(req.getRemark());
            msgParam.setPathUrl(weChatAppletInfraService.getWeChatAppletUrlLink(null, null));
            msgParam.setTenantId(operator.getTenantId());
            msgParam.setNotifyUsers(userContact);
            msgDomainService.sendCrmMsg(msgParam);
        }

        return true;
    }

    public PageResp<WfFormPageResp> getByPage(WfFormPageReq req) {
        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();
        Assert.notNull(userInfoDto);

//        if (AuthVerifyUtils.notSuperAdmin() && ObjUtil.notEqual(userInfoDto.getTenantId(), req.getTenantId())) {
//            throw new ServiceException("非法请求，不允许查看其他租户信息");
//        }

        //数据权限校验，超级管理员可查询全部租户数据，非超管默认只能查询同一租户下的数据
        if (AuthVerifyUtils.notSuperAdmin() && ObjUtil.isNull(req.getTenantId())) {
            req.setTenantId(userInfoDto.getTenantId());
        }

        IPage<WorkOrderListParam> reqIpage = new Page<>(req.getCurrentPage(), req.getPageSize());
        WorkOrderListParam workOrderListParam = BeanUtil.copyProperties(req, WorkOrderListParam.class);
        DataPermissionRangeTypeEnums dataEnums = dataPermissionDomainService.getCurrentUserDataPermission(BizFunctionEnums.BIZORDER_LIST);

        IPage<FormResult> pages = wfFormDao.findListForWx(reqIpage, workOrderListParam, userInfoDto, dataEnums);
        List<WfFormPageResp> resps = BeanUtil.copyToList(pages.getRecords(), WfFormPageResp.class);

        return new PageResp<WfFormPageResp>()
                .setList(resps)
                .setCurrentPage(pages.getCurrent())
                .setPageSize(pages.getSize())
                .setTotal(pages.getTotal());

    }

    public List<FormResult> findAll(WfFormPageReq req) {
        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();
        Assert.notNull(userInfoDto);

        //数据权限校验，超级管理员可查询全部租户数据，非超管默认只能查询同一租户下的数据
        if (AuthVerifyUtils.notSuperAdmin() && ObjUtil.isNull(req.getTenantId())) {
            req.setTenantId(userInfoDto.getTenantId());
        }

        WorkOrderListParam workOrderListParam = BeanUtil.copyProperties(req, WorkOrderListParam.class);
        DataPermissionRangeTypeEnums dataEnums = dataPermissionDomainService.getCurrentUserDataPermission(BizFunctionEnums.BIZORDER_LIST);

        return wfFormDao.findAll(workOrderListParam, userInfoDto, dataEnums);
    }

    public PageResp<FormWithdrawReturnResp> findWithdrawReturnList(WfFormPageReq req) {
        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();
        Assert.notNull(userInfoDto);
        IPage<WorkOrderListParam> reqIpage = new Page<>(req.getCurrentPage(), req.getPageSize());
        WorkOrderListParam workOrderListParam = BeanUtil.copyProperties(req, WorkOrderListParam.class);
        DataPermissionRangeTypeEnums dataEnums = dataPermissionDomainService.getCurrentUserDataPermission(BizFunctionEnums.BIZORDER_LIST);
        IPage<FormWithdrawReturnResult> pages = wfFormDao.findWithdrawReturnList(reqIpage, workOrderListParam, userInfoDto, dataEnums);
        List<FormWithdrawReturnResp> resps = BeanUtil.copyToList(pages.getRecords(), FormWithdrawReturnResp.class);
        return new PageResp<FormWithdrawReturnResp>()
                .setList(resps)
                .setCurrentPage(pages.getCurrent())
                .setPageSize(pages.getSize())
                .setTotal(pages.getTotal());
    }

    public WfFormFlowResp getFlowByFormId(WfFormFlowReq req) {
        WfForm wfForm = wfFormDao.getById(req.getFormId());
        if (ObjectUtil.isNull(wfForm)) {
            throw new ServiceException("工单不存在!");
        }

        UserInfoDto operator = UserInfoContextUtils.getCurrentUserInfo();

        //获取流程节点
        List<FlowResult> flowList = wfFormDao.getFlowByFormId(req.getFormId(), operator);
        if (CollUtil.isEmpty(flowList)) {
            throw new ServiceException("权限不足!");
        }

        //处理流程节点评论
        WorkFlowCommentUtil.handleWorkFlowComment(flowList);

        //获取工单操作按钮
        DataPermissionRangeTypeEnums dataEnums = dataPermissionDomainService.getCurrentUserDataPermission(BizFunctionEnums.CONTRACT_LIST);
        List<ContractResult> contractList = scServiceContractDao.form(wfForm.getFormId(), operator, dataEnums);
        List<BaseButton> buttonList = ConsultingButtonHelper.getWorkOrderButton(wfForm, flowList, contractList, operator);

        return new WfFormFlowResp()
                .setButtonList(buttonList)
                .setWfFormFlowList(BeanUtil.copyToList(flowList, WfFormFlowListResp.class));
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean updateWorkOrder(WorkOrderUpdateReq req) {
        //当前操作时间
        LocalDateTime now = LocalDateTime.now();

        //获取当前登录操作用户信息
        UserInfoDto operator = UserInfoContextUtils.getCurrentUserInfo();

        Integer formId = req.getFormId();
        String lockKey = redisFront + "form_" + formId;
        String requestId = IdUtil.fastSimpleUUID();

        try {
            boolean locked = redisLockUtils.lock(lockKey, requestId, 60L);
            if (!locked) {
                throw new ServiceException("工单已锁定，请勿同时操作!");
            }

            //工单校验
            WfForm wfForm = wfFormDao.getOne(Wrappers.<WfForm>lambdaQuery().eq(WfForm::getFormId, formId).eq(WfForm::getTenantId, operator.getTenantId()));
            if (ObjectUtil.isNull(wfForm)) {
                throw new ServiceException("工单不存在!");
            }

            //获取工单流转记录，时间倒序取最新的记录
            WfFormFlow lastFlow = wfFormFlowDao.getLatestFormFlow(formId, operator.getTenantId());

            return switch (req.getType()) {

                //分派工单：集团客服分派工单给二级公司客服；form表status变为处理中，form表subStatus变为待处理；flow表新增记录status为流转二级公司处理；下一步由二级公司客服处理
                case 1 -> assignOrder(req, wfForm, lastFlow.getActionType(), operator, now);

                //回复工单：二级公司客服处理工单，form表status和subStatus变为已处理；flow表新增记录status为已回复
                case 2 -> handleOrder(req, wfForm, lastFlow.getActionType(), operator, now);

                //完成工单：form表status和subStatus变为已终止；flow表新增记录status为已完成; 已跟前端确认未使用
                case 3 -> finishOrder(req, wfForm, lastFlow.getActionType(), operator, now);

                //终止工单：form表status和subStatus变为已终止；flow表新增记录status为已完成；关联合同的状态也会变为已终止
                case 4 -> terminateOrder(req, wfForm, lastFlow.getActionType(), operator, now);

                //撤回工单：集团客服撤回已到二级公司客服的工单，form表status和subStatus变为待处理；flow表新增记录status为流转集团处理；下一步由集团客服处理
                case 5 -> withdrawOrder(req, wfForm, lastFlow.getActionType(), operator, now);

                //退回工单：二级公司客服退回给集团客服，form表status和subStatus变为待处理；flow表新增记录status为流转集团处理；下一步由集团客服处理
                case 6 -> returnOrder(req, wfForm, lastFlow.getActionType(), operator, now);

                default -> false;
            };

        } finally {
            redisLockUtils.unlock(lockKey, requestId);
        }
    }

    private Boolean assignOrder(WorkOrderUpdateReq req, WfForm wfForm, String lastFlowActionType, UserInfoDto operator, LocalDateTime now) {
        if (lastFlowActionType.equals(ConstParam.FlowCompanyProcess)) {
            throw new ServiceException("工单已经流转!");
        }
        Long operatorUserId = operator.getUserId();
        Integer formId = wfForm.getFormId();
        BizOrderFromContentDto fromContent = JSONUtil.toBean(wfForm.getContent(), BizOrderFromContentDto.class);
        Integer customerUserId = wfForm.getCreateUserId();
        Integer currentOrgId = wfForm.getCurrentOrgId();
        List<RoleUserResult> assignUsers = bUserDao.getUserByRoleCodeAndOrgId(RoleEnums.SUB_COMPANY_CUSTOMER.getCode(), req.getCurrentOrgId(), operator.getTenantId());
        if (assignUsers.isEmpty()) {
            throw new ServiceException("没有客服角色!");
        }
        //待二级公司客服处理工单
        Integer currentRoleId = bRoleDao.getRoleIdByCode(RoleEnums.SUB_COMPANY_CUSTOMER.getCode(), operator.getTenantId());
        //变更工单信息
        LambdaUpdateWrapper<WfForm> updateWrapper = Wrappers.<WfForm>update().lambda()
                .set(WfForm::getStatus, ConstParam.Processing)
                .set(WfForm::getSubStatus, ConstParam.WaitingForProcessing)
                .set(WfForm::getTimeReception, now)
                .set(WfForm::getModifyUserId, operatorUserId)
                .set(WfForm::getCurrentOrgId, req.getCurrentOrgId())
                .set(WfForm::getCurrentRoleId, currentRoleId)
                .eq(WfForm::getFormId, formId);
        wfFormDao.update(updateWrapper);
        //新增工单流转分派记录
        WfFormFlow wfFormFlow = new WfFormFlow()
                .setFormId(formId)
                .setUserId(operatorUserId.intValue())
                .setTimeAction(now)
                .setActionType(ConstParam.FlowCompanyProcess)
                .setActionContent(req.getContent())
                .setStatus(ConstParam.FlowCompanyProcess)
                .setCreateUserId(operatorUserId.intValue())
                .setSubStatus(req.getLevel().equals(1) ? ConstParam.FlowTagGroup : ConstParam.FlowTagSub)
                .setTenantId(operator.getTenantId());
        wfFormFlowDao.save(wfFormFlow);

        //发送elink消息通知，通知二级公司客服
        BizOrderAssignMsgToRoleParam msgParam = new BizOrderAssignMsgToRoleParam();
        List<RoleEnums> roleEnums = dataPermissionDomainService.getFunctionRoleEnums(operator.getTenantId(), msgParam.getFunctionCode());
        if (!roleEnums.isEmpty()) {
            List<UserContactDto> userContact = userDomainService.getUserContact(roleEnums, req.getCurrentOrgId(), operator.getTenantId());
            msgParam.setOrderName(fromContent.getBusinessName());
            msgParam.setOrderCode(fromContent.getCode());
            msgParam.setAssignTime(now.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
            msgParam.setCustomerName(fromContent.getCustomerName());
            msgParam.setMobile(fromContent.getMobile());
            msgParam.setPathUrl(weChatAppletInfraService.getWeChatAppletUrlLink(null, null));
            msgParam.setTenantId(operator.getTenantId());
            msgParam.setNotifyUsers(userContact);
            msgDomainService.sendCrmMsg(msgParam);

            if (roleEnums.stream().map(RoleEnums::getCode).toList().contains(RoleEnums.APPLET_USER.getCode())) {
                //发送微信小程序消息，通知客户
                userContact = userDomainService.getUserContact(customerUserId, operator.getTenantId());
                msgDomainService.sendCrmMsg(new BizOrderAssignMsgToUserParam()
                        .setOrderName(fromContent.getBusinessName())
                        .setOrderCode(fromContent.getCode())
                        .setServiceUnit(bOrganizationDao.getById(currentOrgId).getName())
                        .setServicePerson(bUserDao.getById(operatorUserId).getRealname())
                        .setStatus(ConstParam.FlowGroupAssign)
                        .setAssignTime(now.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)))
                        .setPathUrl(weChatAppletInfraService.getWeChatAppletUrlLink(null, null))
                        .setTenantId(operator.getTenantId())
                        .setNotifyUsers(userContact)
                );
            }
        }


        return true;
    }

    private Boolean handleOrder(WorkOrderUpdateReq req, WfForm wfForm, String lastFlowActionType, UserInfoDto operator, LocalDateTime now) {
        if (lastFlowActionType.equals(ConstParam.FlowGroupProcess)) {
            throw new ServiceException("工单已经撤回!");
        }

        if (lastFlowActionType.equals(ConstParam.FlowFinished)) {
            throw new ServiceException("工单已完成，不能再次处理!");
        }

        if (lastFlowActionType.equals(ConstParam.Terminated)) {
            throw new ServiceException("工单已终止，不能再次处理!");
        }

        Long operatorUserId = operator.getUserId();
        Integer formId = wfForm.getFormId();
        Integer currentOrgId = wfForm.getCurrentOrgId();
        //二级公司处理工单
        Integer currentRoleId = bRoleDao.getRoleIdByCode(RoleEnums.SUB_COMPANY_CUSTOMER.getCode(), operator.getTenantId());
        //变更工单信息
        LambdaUpdateWrapper<WfForm> updateWrapper = Wrappers.<WfForm>update().lambda()
                .set(WfForm::getStatus, ConstParam.Processed)
                .set(WfForm::getSubStatus, ConstParam.Processed)
                .set(WfForm::getTimeReception, now)
                .set(WfForm::getModifyUserId, operatorUserId)
                .set(WfForm::getCurrentOrgId, req.getCurrentOrgId())
                .set(WfForm::getCurrentRoleId, currentRoleId)
                .eq(WfForm::getFormId, formId);
        wfFormDao.update(updateWrapper);
        //新增工单流转处理记录
        WfFormFlow wfFormFlow = new WfFormFlow()
                .setFormId(formId)
                .setUserId(operatorUserId.intValue())
                .setTimeAction(now)
                .setActionType(ConstParam.FlowHasFeedback)
                .setActionContent(req.getContent())
                .setStatus(ConstParam.FlowHasFeedback)
                .setCreateUserId(operatorUserId.intValue())
                .setSubStatus(req.getLevel().equals(1) ? ConstParam.FlowTagGroup : ConstParam.FlowTagSub)
                .setTenantId(operator.getTenantId())
                .setServiceUnitId(operator.getOrganizationList().iterator().next().getId());
        wfFormFlowDao.save(wfFormFlow);

        //发送微信小程序消息，通知客户
        BizOrderFromContentDto fromContent = JSONUtil.toBean(wfForm.getContent(), BizOrderFromContentDto.class);
        BizOrderHandleMsgToUserParam msgParam = new BizOrderHandleMsgToUserParam();
        List<RoleEnums> roleEnums = dataPermissionDomainService.getFunctionRoleEnums(operator.getTenantId(), msgParam.getFunctionCode());
        if (roleEnums.stream().map(RoleEnums::getCode).toList().contains(RoleEnums.APPLET_USER.getCode())) {
            List<UserContactDto> userContact = userDomainService.getUserContact(wfForm.getCreateUserId(), operator.getTenantId());
            msgDomainService.sendCrmMsg(msgParam.setOrderName(fromContent.getBusinessName())
                    .setOrderCode(fromContent.getCode())
                    .setServiceUnit(bOrganizationDao.getById(currentOrgId).getName())
                    .setServicePerson(bUserDao.getById(operatorUserId).getRealname())
                    .setStatus(ConstParam.FlowHasFeedback)
                    .setHandleTime(now.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)))
                    .setPathUrl(weChatAppletInfraService.getWeChatAppletUrlLink(null, null))
                    .setTenantId(operator.getTenantId())
                    .setNotifyUsers(userContact)
            );
        }

        return true;
    }

    private Boolean finishOrder(WorkOrderUpdateReq req, WfForm wfForm, String lastFlowActionType, UserInfoDto operator, LocalDateTime now) {
        if (lastFlowActionType.equals(ConstParam.FlowFinished)) {
            throw new ServiceException("工单已经完成，不能重复完成!");
        }

        if(StrUtil.equals(wfForm.getSubStatus(), ConstParam.WaitingForProcessing)) {
            throw new ServiceException("工单未处理，不能完成!");
        }

        Long operatorUserId = operator.getUserId();
        Integer formId = wfForm.getFormId();
        Integer customerUserId = wfForm.getCreateUserId();
        Integer currentOrgId = wfForm.getCurrentOrgId();

        //判断合同是否全部已竣工
        DataPermissionRangeTypeEnums dataEnums = dataPermissionDomainService.getCurrentUserDataPermission(BizFunctionEnums.CONTRACT_LIST);
        List<ContractResult> resultList = scServiceContractDao.form(formId, operator, dataEnums);
        if (resultList.stream().anyMatch(contractResult -> contractResult.getStatus().equals(ConstParam.ContractNotBegin) ||
                contractResult.getStatus().equals(ConstParam.ContractUnderWay))) {
            throw new ServiceException("该工单有未竣工合同，不能完成工单!");
        }

        //后续由集团客服操作完成工单，待定
        Integer currentRoleId = bRoleDao.getRoleIdByCode(RoleEnums.JT_CUSTOMER.getCode(), operator.getTenantId());
        //变更工单信息
        LambdaUpdateWrapper<WfForm> updateWrapper = Wrappers.<WfForm>update().lambda()
                .set(WfForm::getStatus, ConstParam.Finished)
                .set(WfForm::getSubStatus, ConstParam.Finished)
                .set(WfForm::getTimeFinished, now)
                .set(WfForm::getModifyUserId, operatorUserId)
                .set(WfForm::getCurrentOrgId, req.getCurrentOrgId())
                .set(WfForm::getCurrentRoleId, currentRoleId)
                .eq(WfForm::getFormId, formId);
        wfFormDao.update(updateWrapper);
        //新增工单流转完成记录
        WfFormFlow wfFormFlow = new WfFormFlow()
                .setFormId(formId)
                .setUserId(operatorUserId.intValue())
                .setTimeAction(now)
                .setActionType(ConstParam.FlowFinished)
                .setActionContent(req.getContent())
                .setStatus(ConstParam.FlowFinished)
                .setCreateUserId(operatorUserId.intValue())
                .setSubStatus(req.getLevel().equals(1) ? ConstParam.FlowTagGroup : ConstParam.FlowTagSub)
                .setTenantId(operator.getTenantId())
                .setServiceUnitId(operator.getOrganizationList().iterator().next().getId());
        wfFormFlowDao.save(wfFormFlow);

        //发送微信小程序消息，通知客户
        BizOrderFromContentDto fromContent = JSONUtil.toBean(wfForm.getContent(), BizOrderFromContentDto.class);
        BizOrderFinishMsgToUserParam msgParam = new BizOrderFinishMsgToUserParam();
        List<RoleEnums> roleEnums = dataPermissionDomainService.getFunctionRoleEnums(operator.getTenantId(), msgParam.getFunctionCode());
        if (roleEnums.stream().map(RoleEnums::getCode).toList().contains(RoleEnums.APPLET_USER.getCode())) {
            List<UserContactDto> userContact = userDomainService.getUserContact(customerUserId, operator.getTenantId());
            msgDomainService.sendCrmMsg(msgParam.setOrderName(fromContent.getBusinessName())
                    .setOrderCode(fromContent.getCode())
                    .setServiceUnit(bOrganizationDao.getById(currentOrgId).getName())
                    .setServicePerson(bUserDao.getById(operatorUserId).getRealname())
                    .setStatus(ConstParam.FlowFinished)
                    .setFinishTime(now.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)))
                    .setPathUrl(weChatAppletInfraService.getWeChatAppletUrlLink(null, null))
                    .setTenantId(operator.getTenantId())
                    .setNotifyUsers(userContact)
            );
        }

        return true;
    }

    private Boolean terminateOrder(WorkOrderUpdateReq req, WfForm wfForm, String lastFlowActionType, UserInfoDto operator, LocalDateTime now) {
        if(!StrUtil.equals(wfForm.getStatus(), ConstParam.WaitingForProcessing) &&
                !StrUtil.equals(wfForm.getStatus(), ConstParam.Processing)){
            throw new ServiceException("工单正在处理中，不能终止!");
        }

        if (lastFlowActionType.equals(ConstParam.Terminated)) {
            throw new ServiceException("工单已终止，不能重复终止!");
        }
        Long operatorUserId = operator.getUserId();
        Integer currentRoleId = bRoleDao.getRoleIdByCode(operator.getRoleCodes().iterator().next(), operator.getTenantId());
        Integer formId = wfForm.getFormId();
        Integer customerUserId = wfForm.getCreateUserId();
        Integer currentOrgId = wfForm.getCurrentOrgId();
        //终止工单
        LambdaUpdateWrapper<WfForm> wfUpdateWrapper = Wrappers.<WfForm>update().lambda()
                .set(WfForm::getStatus, ConstParam.Terminated)
                .set(WfForm::getSubStatus, ConstParam.Terminated)
                .set(WfForm::getTimeFinished, now)
                .set(WfForm::getModifyUserId, operatorUserId)
                .set(WfForm::getCurrentOrgId, req.getCurrentOrgId())
                .set(WfForm::getCurrentRoleId, currentRoleId)
                .eq(WfForm::getFormId, formId);
        wfFormDao.update(wfUpdateWrapper);
        //新增工单流转终止记录
        WfFormFlow wfFormFlow = new WfFormFlow()
                .setFormId(formId)
                .setUserId(operatorUserId.intValue())
                .setTimeAction(now)
                .setActionType(ConstParam.FlowTerminated)
                .setActionContent(req.getContent())
                .setStatus(ConstParam.FlowTerminated)
                .setCreateUserId(operatorUserId.intValue())
                .setSubStatus(req.getLevel().equals(1) ? ConstParam.FlowTagGroup : ConstParam.FlowTagSub)
                .setTenantId(operator.getTenantId())
                .setServiceUnitId(operator.getOrganizationList().iterator().next().getId());
        wfFormFlowDao.save(wfFormFlow);
        //终止合同
        LambdaUpdateWrapper<ScServiceContract> sscUpdateWrapper = Wrappers.<ScServiceContract>update().lambda()
                .set(ScServiceContract::getStatus, ConstParam.ContractDiscontinued)
                .eq(ScServiceContract::getFormId, formId)
                .and(i -> i.eq(ScServiceContract::getStatus, ConstParam.ContractNotBegin).or().eq(ScServiceContract::getStatus, ConstParam.ContractUnderWay));
        scServiceContractDao.update(sscUpdateWrapper);

        //发送微信小程序消息，通知客户
        BizOrderFromContentDto fromContent = JSONUtil.toBean(wfForm.getContent(), BizOrderFromContentDto.class);
        BizOrderTerminateMsgToUserParam msgParam = new BizOrderTerminateMsgToUserParam();
        List<RoleEnums> roleEnums = dataPermissionDomainService.getFunctionRoleEnums(operator.getTenantId(), msgParam.getFunctionCode());
        if (roleEnums.stream().map(RoleEnums::getCode).toList().contains(RoleEnums.APPLET_USER.getCode())) {
            List<UserContactDto> userContact = userDomainService.getUserContact(customerUserId, operator.getTenantId());
            msgDomainService.sendCrmMsg(msgParam.setOrderName(fromContent.getBusinessName())
                    .setOrderCode(fromContent.getCode())
                    .setServiceUnit(bOrganizationDao.getById(currentOrgId).getName())
                    .setServicePerson(bUserDao.getById(operatorUserId).getRealname())
                    .setStatus(ConstParam.FlowFinished)
                    .setTerminateTime(now.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)))
                    .setPathUrl(weChatAppletInfraService.getWeChatAppletUrlLink(null, null))
                    .setTenantId(operator.getTenantId())
                    .setNotifyUsers(userContact)
            );
        }

        return true;
    }

    //撤回工单：集团客服撤回已到二级公司客服的工单，form表status和subStatus变为待处理；flow表新增记录status为流转集团处理；下一步由集团客服处理
    private Boolean withdrawOrder(WorkOrderUpdateReq req, WfForm wfForm, String lastFlowActionType, UserInfoDto operator, LocalDateTime now) {
        if (lastFlowActionType.equals(ConstParam.FlowStart)) {
            throw new ServiceException("工单未分派，不能撤回!");
        }
        if (lastFlowActionType.equals(ConstParam.FlowHasFeedback)) {
            throw new ServiceException("工单已经回复，不能撤回!");
        }
        if (lastFlowActionType.equals(ConstParam.FlowGroupProcess)) {
            throw new ServiceException("工单已被撤回，不能重复操作!");
        }
        if (lastFlowActionType.equals(ConstParam.FlowFinished)) {
            throw new ServiceException("工单已完成或终止，不能撤回!");
        }
        Long operatorUserId = operator.getUserId();
        Integer formId = wfForm.getFormId();
        BizOrderFromContentDto fromContent = JSONUtil.toBean(wfForm.getContent(), BizOrderFromContentDto.class);
        Integer formCurrentOrgId = wfForm.getCurrentOrgId();
        Integer customerUserId = wfForm.getCreateUserId();

        //撤回到集团客服处理
        Integer currentRoleId = bRoleDao.getRoleIdByCode(RoleEnums.JT_CUSTOMER.getCode(), operator.getTenantId());
        //变更工单信息
        LambdaUpdateWrapper<WfForm> wfUpdateWrapper = Wrappers.<WfForm>update().lambda()
                .set(WfForm::getStatus, ConstParam.WaitingForProcessing)
                .set(WfForm::getSubStatus, ConstParam.WaitingForProcessing)
                .set(WfForm::getTimeReception, now)
                .set(WfForm::getModifyUserId, operatorUserId)
                .set(WfForm::getCurrentOrgId, 1)
                .set(WfForm::getCurrentRoleId, currentRoleId)
                .eq(WfForm::getFormId, formId);
        wfFormDao.update(wfUpdateWrapper);
        //新增工单流转处撤回记录
        WfFormFlow wfFormFlow = new WfFormFlow()
                .setFormId(formId)
                .setUserId(operatorUserId.intValue())
                .setTimeAction(now)
                .setActionType(ConstParam.GroupWithdraw)
                .setActionContent(req.getContent())
                .setStatus(ConstParam.FlowGroupWithdraw)
                .setCreateUserId(operatorUserId.intValue())
                .setTenantId(operator.getTenantId())
                .setServiceUnitId(formCurrentOrgId);
        wfFormFlowDao.save(wfFormFlow);

        //发送elink消息，通知二级公司客服
        BizOrderWithdrawMsgToRoleParam withdrawMsgParam = new BizOrderWithdrawMsgToRoleParam();
        List<RoleEnums> roleEnums = dataPermissionDomainService.getFunctionRoleEnums(operator.getTenantId(), withdrawMsgParam.getFunctionCode());
        if (!roleEnums.isEmpty()) {
            List<UserContactDto> userContact = userDomainService.getUserContact(roleEnums, formCurrentOrgId, operator.getTenantId());
            withdrawMsgParam.setOrderName(fromContent.getBusinessName());
            withdrawMsgParam.setOrderCode(fromContent.getCode());
            withdrawMsgParam.setWithdrawTime(now.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
            withdrawMsgParam.setOperator(RoleEnums.JT_CUSTOMER.getDesc());
            withdrawMsgParam.setContent(req.getContent());
            withdrawMsgParam.setPathUrl(weChatAppletInfraService.getWeChatAppletUrlLink(null, null));
            withdrawMsgParam.setTenantId(operator.getTenantId());
            withdrawMsgParam.setNotifyUsers(userContact);
            msgDomainService.sendCrmMsg(withdrawMsgParam);

            if (roleEnums.stream().map(RoleEnums::getCode).toList().contains(RoleEnums.APPLET_USER.getCode())) {
                //发送微信小程序消息，通知客户
                userContact = userDomainService.getUserContact(customerUserId, operator.getTenantId());
                msgDomainService.sendCrmMsg(new BizOrderWithdrawMsgToUserParam()
                        .setOrderName(fromContent.getBusinessName())
                        .setOrderCode(fromContent.getCode())
                        .setServiceUnit(bOrganizationDao.getById(req.getCurrentOrgId()).getName())
                        .setServicePerson(bUserDao.getById(operatorUserId).getRealname())
                        .setStatus(ConstParam.FlowGroupWithdraw)
                        .setWithdrawTime(now.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)))
                        .setPathUrl(weChatAppletInfraService.getWeChatAppletUrlLink(null, null))
                        .setTenantId(operator.getTenantId())
                        .setNotifyUsers(userContact)
                );
            }
        }

        return true;
    }

    //退回工单：二级公司客服退回给集团客服，form表status和subStatus变为待处理；flow表新增记录status为流转集团处理；下一步由集团客服处理
    private Boolean returnOrder(WorkOrderUpdateReq req, WfForm wfForm, String lastFlowActionType, UserInfoDto operator, LocalDateTime now) {
//        if (lastFlowActionType.equals(ConstParam.FlowHasFeedback)) {
//            throw new ServiceException("工单已经回复，不能退回!");
//        }
        if (lastFlowActionType.equals(ConstParam.FlowGroupProcess)) {
            throw new ServiceException("工单已被撤回，不能重复操作!");
        }
        if (lastFlowActionType.equals(ConstParam.FlowFinished)) {
            throw new ServiceException("工单已完成或终止，不能退回!");
        }

        //添加过合同不能退回
        DataPermissionRangeTypeEnums dataEnums = dataPermissionDomainService.getCurrentUserDataPermission(BizFunctionEnums.CONTRACT_LIST);
        List<ContractResult> resultList = scServiceContractDao.form(req.getFormId(), operator, dataEnums);
        if (CollUtil.isNotEmpty(resultList)) {
            throw new ServiceException("工单已添加过合同，不能退回!");
        }

        Long operatorUserId = operator.getUserId();
        Integer formId = wfForm.getFormId();
        BizOrderFromContentDto fromContent = JSONUtil.toBean(wfForm.getContent(), BizOrderFromContentDto.class);
        Integer customerUserId = wfForm.getCreateUserId();
        Integer currentOrgId = wfForm.getCurrentOrgId();
        //撤回到集团客服处理
        Integer currentRoleId = bRoleDao.getRoleIdByCode(RoleEnums.JT_CUSTOMER.getCode(), operator.getTenantId());
        //变更工单信息
        LambdaUpdateWrapper<WfForm> wfUpdateWrapper = Wrappers.<WfForm>update().lambda()
                .set(WfForm::getStatus, ConstParam.WaitingForProcessing)
                .set(WfForm::getSubStatus, ConstParam.WaitingForProcessing)
                .set(WfForm::getTimeReception, now)
                .set(WfForm::getModifyUserId, operatorUserId)
                .set(WfForm::getCurrentOrgId, 1)
                .set(WfForm::getCurrentRoleId, currentRoleId)
                .eq(WfForm::getFormId, formId);
        wfFormDao.update(wfUpdateWrapper);
        //新增工单流转处撤回记录
        WfFormFlow wfFormFlow = new WfFormFlow()
                .setFormId(formId)
                .setUserId(operatorUserId.intValue())
                .setTimeAction(now)
                .setActionType(ConstParam.CompanyReturn)
                .setActionContent(req.getContent())
                .setStatus(ConstParam.FlowCompanyReturn)
                .setCreateUserId(operatorUserId.intValue())
                .setTenantId(operator.getTenantId())
                .setServiceUnitId(operator.getOrganizationList().iterator().next().getId());
        wfFormFlowDao.save(wfFormFlow);

        //发送elink消息，通知集团客服
        BizOrderReturnMsgToRoleParam msgParam = new BizOrderReturnMsgToRoleParam();
        List<RoleEnums> roleEnums = dataPermissionDomainService.getFunctionRoleEnums(operator.getTenantId(), msgParam.getFunctionCode());
        if (!roleEnums.isEmpty()) {
            String serviceUnit = bOrganizationDao.getById(currentOrgId).getName();
            List<UserContactDto> userContact = userDomainService.getUserContact(roleEnums, operator.getTenantId());
            msgParam.setOrderName(fromContent.getBusinessName());
            msgParam.setOrderCode(fromContent.getCode());
            msgParam.setReturnTime(now.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
            msgParam.setCompanyName(serviceUnit);
            msgParam.setContent(req.getContent());
            msgParam.setPathUrl(weChatAppletInfraService.getWeChatAppletUrlLink(null, null));
            msgParam.setTenantId(operator.getTenantId());
            msgParam.setNotifyUsers(userContact);
            msgDomainService.sendCrmMsg(msgParam);

            if (roleEnums.stream().map(RoleEnums::getCode).toList().contains(RoleEnums.APPLET_USER.getCode())) {
                //发送微信小程序消息，通知客户
                userContact = userDomainService.getUserContact(customerUserId, operator.getTenantId());
                msgDomainService.sendCrmMsg(new BizOrderReturnMsgToUserParam()
                        .setOrderName(fromContent.getBusinessName())
                        .setOrderCode(fromContent.getCode())
                        .setServiceUnit(serviceUnit)
                        .setServicePerson(bUserDao.getById(operatorUserId).getRealname())
                        .setStatus(ConstParam.FlowCompanyReturn)
                        .setReturnTime(now.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)))
                        .setPathUrl(weChatAppletInfraService.getWeChatAppletUrlLink(null, null))
                        .setTenantId(operator.getTenantId())
                        .setNotifyUsers(userContact)
                );
            }
        }

        return true;
    }

    private String genOrderCode() {
        //生成工单编号 yyyyMMdd+4位随机数
        String dateStr = DateFormatUtils.format(Calendar.getInstance().getTime(), DatePattern.PURE_DATE_PATTERN);
        String randomStr = RandomUtil.randomString(4);
        String code = dateStr + randomStr;
        boolean isExistCode = redisUtils.hasKey(WORK_CODE_CACHE_KEY_PREFIX + code);
        if (isExistCode) {
            this.genOrderCode();
        }
        redisUtils.setEx(WORK_CODE_CACHE_KEY_PREFIX + code, code, 24, TimeUnit.HOURS);
        return code;
    }

}
