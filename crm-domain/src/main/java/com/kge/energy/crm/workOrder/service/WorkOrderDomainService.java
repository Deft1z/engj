package com.kge.energy.crm.workOrder.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.*;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.common.button.enums.WorkOrderButtonEnum;
import com.kge.energy.crm.common.button.helper.ConsultingButtonHelper;
import com.kge.energy.crm.common.button.resp.BaseButton;
import com.kge.energy.crm.common.constans.ConstParam;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.AuthVerifyUtils;
import com.kge.energy.crm.common.util.RedisLockUtils;
import com.kge.energy.crm.common.util.RedisUtils;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.BizFunctionEnums;
import com.kge.energy.crm.enums.DataPermissionRangeTypeEnums;
import com.kge.energy.crm.enums.RoleEnums;
import com.kge.energy.crm.external.elink.ElinkService;
import com.kge.energy.crm.external.wechat.applet.property.WeChatAppletProperties;
import com.kge.energy.crm.external.wechat.applet.req.FormStatusChangeMsgReq;
import com.kge.energy.crm.external.wechat.applet.req.SendSubscribeReq;
import com.kge.energy.crm.external.wechat.applet.service.WeChatAppletInfraService;
import com.kge.energy.crm.permission.service.DataPermissionDomainService;
import com.kge.energy.crm.repository.dao.*;
import com.kge.energy.crm.repository.entity.*;
import com.kge.energy.crm.repository.entityext.param.WorkOrderListParam;
import com.kge.energy.crm.repository.entityext.result.*;
import com.kge.energy.crm.workOrder.req.WfFormFlowReq;
import com.kge.energy.crm.workOrder.req.WfFormPageReq;
import com.kge.energy.crm.workOrder.req.WorkOrderAddReq;
import com.kge.energy.crm.workOrder.req.WorkOrderUpdateReq;
import com.kge.energy.crm.workOrder.resp.FormWithdrawReturnResp;
import com.kge.energy.crm.workOrder.resp.WfFormFlowListResp;
import com.kge.energy.crm.workOrder.resp.WfFormFlowResp;
import com.kge.energy.crm.workOrder.resp.WfFormPageResp;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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
    private final WeChatAppletProperties weChatAppletProperties;
    private final WeChatAppletInfraService weChatAppletInfraService;
    private final ElinkService elinkService;
    private final DataPermissionDomainService dataPermissionDomainService;

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

        //todo 使用流程引擎替换现有的流程业务


        //发送消息，通知集团客服
        final String msgTitle = activeProfile.contains("prod") ? "工单待处理通知" : "工单待处理通知（体验版）";
        String msgContent = "工单名称：" + content.getBusinessName() + "\\n" +
                "所在地区：" + content.getArea() + "\\n" +
                "用电容量(kVA)：" + content.getElectricityCapacity() + "\\n" +
                "工单编号：" + content.getCode() + "\\n" +
                "生成时间：" + now.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)) + "\\n" +
                "客户名称：" + content.getCustomerName() + "\\n" +
                "客户手机：" + content.getMobile() + "\\n" +
                "备注：" + req.getRemark();
        //获取集团客服人员手机号
        List<String> phones = bUserDao.findJtCustomerPhones(operator.getTenantId());
        sendGroupCustomerElinkMsg(wfForm, phones, msgTitle, msgContent);

        return true;
    }

    public PageResp<WfFormPageResp> getByPage (WfFormPageReq req) {
        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();
        Assert.notNull(userInfoDto);

        if (AuthVerifyUtils.notSuperAdmin() && ObjUtil.notEqual(userInfoDto.getTenantId(), req.getTenantId())) {
            throw new ServiceException("非法请求，不允许查看其他租户信息");
        }

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

    public WfFormFlowResp getFlowByFormId (WfFormFlowReq req) {
        UserInfoDto operator = UserInfoContextUtils.getCurrentUserInfo();
        List<FlowResult> list = wfFormDao.getFlowByFormId(req.getFormId(), operator);
        if (CollUtil.isEmpty(list)) {
            throw new ServiceException("权限不足!");
        }

        List<WfFormFlowListResp> wfFormFlowListRespList = BeanUtil.copyToList(list, WfFormFlowListResp.class);

        //返回工单操作按钮
        List<BaseButton> buttonList = new ArrayList<>();
        if (operator.getRoleCodes().contains(RoleEnums.JT_CUSTOMER.getCode())) {
            //集团客服按钮
            buttonList.add(ConsultingButtonHelper.getWorkOrderButton(WorkOrderButtonEnum.TERMINATE_WORK_ORDER));
            buttonList.add(ConsultingButtonHelper.getWorkOrderButton(WorkOrderButtonEnum.WITHDRAW_WORK_ORDER));
            buttonList.add(ConsultingButtonHelper.getWorkOrderButton(WorkOrderButtonEnum.ASSIGN_WORK_ORDER));
        } else if (operator.getRoleCodes().contains(RoleEnums.SUB_COMPANY_CUSTOMER.getCode())) {
            //二级公司客服按钮
            buttonList.add(ConsultingButtonHelper.getWorkOrderButton(WorkOrderButtonEnum.FINISH_WORK_ORDER));
            buttonList.add(ConsultingButtonHelper.getWorkOrderButton(WorkOrderButtonEnum.RETURN_WORK_ORDER));
            buttonList.add(ConsultingButtonHelper.getWorkOrderButton(WorkOrderButtonEnum.HANDLE_WORK_ORDER));
        }

        return new WfFormFlowResp()
                .setButtonList(buttonList)
                .setWfFormFlowList(wfFormFlowListRespList);
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
        String formContent = wfForm.getContent();
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
                .setTenantId(operator.getTenantId())
                .setServiceUnitId(operator.getOrganizationList().iterator().next().getId());
        wfFormFlowDao.save(wfFormFlow);

        //TODO: 发送微信小程序消息通知提单的客户

        //发送elink消息通知，通知二级公司客服
        sendSubCompanyCustomerElinkMsg(wfForm, assignUsers, now);

        return true;
    }

    private Boolean handleOrder(WorkOrderUpdateReq req, WfForm wfForm, String lastFlowActionType, UserInfoDto operator, LocalDateTime now) {
        if (lastFlowActionType.equals(ConstParam.FlowGroupProcess)) {
            throw new ServiceException("工单已经撤回!");
        }
        Long operatorUserId = operator.getUserId();
        Integer formId = wfForm.getFormId();
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
        sendFormStatusChangeMsg(req, operator, wfForm, now, ConstParam.FlowHasFeedback);

        return true;
    }

    private Boolean finishOrder(WorkOrderUpdateReq req, WfForm wfForm, String lastFlowActionType, UserInfoDto operator, LocalDateTime now) {
        if (lastFlowActionType.equals(ConstParam.FlowFinished)) {
            throw new ServiceException("工单已经完成，不能重复完成!");
        }
        Long operatorUserId = operator.getUserId();
        Integer formId = wfForm.getFormId();
        Integer customerUserId = wfForm.getCreateUserId();

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
        sendFormStatusChangeMsg(req, operator, wfForm, now, ConstParam.FlowFinished);

        return true;
    }

    private Boolean terminateOrder(WorkOrderUpdateReq req, WfForm wfForm, String lastFlowActionType, UserInfoDto operator, LocalDateTime now) {
        if (lastFlowActionType.equals(ConstParam.FlowFinished)) {
            throw new ServiceException("工单已经完成或终止，不能重复完成或终止!");
        }
        Long operatorUserId = operator.getUserId();
        Integer currentRoleId = bRoleDao.getRoleIdByCode(operator.getRoleCodes().iterator().next(), operator.getTenantId());
        Integer formId = wfForm.getFormId();
        Integer customerUserId = wfForm.getCreateUserId();
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
                .setActionType(ConstParam.FlowTerminatedd)
                .setActionContent(req.getContent())
                .setStatus(ConstParam.FlowTerminatedd)
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
        sendFormStatusChangeMsg(req, operator, wfForm, now, ConstParam.Finished);

        return true;
    }

    //撤回工单：集团客服撤回已到二级公司客服的工单，form表status和subStatus变为待处理；flow表新增记录status为流转集团处理；下一步由集团客服处理
    private Boolean withdrawOrder(WorkOrderUpdateReq req, WfForm wfForm, String lastFlowActionType, UserInfoDto operator, LocalDateTime now) {
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
        String formContent = wfForm.getContent();
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

        //发送微信小程序消息，通知客户
        sendFormStatusChangeMsg(req, operator, wfForm, now, ConstParam.SendBack);

        //发送elink消息，通知集团客服
        final String msgTitle = activeProfile.contains("prod") ? "工单撤回通知" : "工单撤回通知（体验版）";
        JSONObject content = JSONUtil.parseObj(formContent);
        StringBuilder msgContentBuilder = new StringBuilder();
        msgContentBuilder.append("工单名称：").append(content.get("businessName")).append("\\n");
        msgContentBuilder.append("工单编号：").append(content.get("code")).append("\\n");
        msgContentBuilder.append("撤回时间：").append(now.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN))).append("\\n");
        msgContentBuilder.append("派发公司：").append(content.get("companyName")).append("\\n");
        msgContentBuilder.append("撤回原因：").append(req.getContent());
        //获取集团客服人员手机号
        List<String> phones = bUserDao.findJtCustomerPhones(operator.getTenantId());
        sendGroupCustomerElinkMsg(wfForm, phones, msgTitle, msgContentBuilder.toString());

        //若集团客服撤回工单，需通知二级公司客服
        if (operator.getRoleCodes().contains(RoleEnums.JT_CUSTOMER.toString())) {
            msgContentBuilder = new StringBuilder();
            msgContentBuilder.append("工单名称：").append(content.get("businessName")).append("\\n");
            msgContentBuilder.append("工单编号：").append(content.get("code")).append("\\n");
            msgContentBuilder.append("撤回时间：").append(now.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN))).append("\\n");
            msgContentBuilder.append("撤回人员：").append(RoleEnums.JT_CUSTOMER.getDesc()).append("\\n");
            msgContentBuilder.append("撤回原因：").append(req.getContent());
            //获取二级公司客服人员手机号
            phones = bUserDao.findSubCustomerPhones(formCurrentOrgId, operator.getTenantId());
            sendGroupCustomerElinkMsg(wfForm, phones, msgTitle, msgContentBuilder.toString());
        }

        return true;
    }

    //退回工单：二级公司客服退回给集团客服，form表status和subStatus变为待处理；flow表新增记录status为流转集团处理；下一步由集团客服处理
    private Boolean returnOrder(WorkOrderUpdateReq req, WfForm wfForm, String lastFlowActionType, UserInfoDto operator, LocalDateTime now) {
        if (lastFlowActionType.equals(ConstParam.FlowHasFeedback)) {
            throw new ServiceException("工单已经回复，不能退回!");
        }
        if (lastFlowActionType.equals(ConstParam.FlowGroupProcess)) {
            throw new ServiceException("工单已被撤回，不能重复操作!");
        }
        if (lastFlowActionType.equals(ConstParam.FlowFinished)) {
            throw new ServiceException("工单已完成或终止，不能退回!");
        }
        Long operatorUserId = operator.getUserId();
        Integer formId = wfForm.getFormId();
        String formContent = wfForm.getContent();
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
                .setActionType(ConstParam.CompanyReturn)
                .setActionContent(req.getContent())
                .setStatus(ConstParam.FlowCompanyReturn)
                .setCreateUserId(operatorUserId.intValue())
                .setTenantId(operator.getTenantId())
                .setServiceUnitId(operator.getOrganizationList().iterator().next().getId());
        wfFormFlowDao.save(wfFormFlow);

        //发送微信小程序消息，通知客户
        sendFormStatusChangeMsg(req, operator, wfForm, now, ConstParam.SendBack);

        //发送elink消息，通知集团客服
        final String msgTitle = activeProfile.contains("prod") ? "工单撤回通知" : "工单撤回通知（体验版）";
        JSONObject content = JSONUtil.parseObj(formContent);
        String msgContentBuilder = "工单名称：" + content.get("businessName") + "\\n" +
                "工单编号：" + content.get("code") + "\\n" +
                "撤回时间：" + now.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)) + "\\n" +
                "派发公司：" + content.get("companyName") + "\\n" +
                "撤回原因：" + req.getContent();
        //获取集团客服人员手机号
        List<String> phones = bUserDao.findJtCustomerPhones(operator.getTenantId());
        sendGroupCustomerElinkMsg(wfForm, phones, msgTitle, msgContentBuilder);

        return true;
    }


    @Async
    private void sendFormStatusChangeMsg (WorkOrderUpdateReq req, UserInfoDto userInfoDto, WfForm form, LocalDateTime sendTime, String status) {
        CompletableFuture.runAsync(() -> {
            try {
                Long operatorUserId = userInfoDto.getUserId();
                Integer customerUserId = form.getCreateUserId();
                BUser customer = bUserDao.getById(customerUserId);
                //服务单位人员信息
                BOrganization serviceOrg = bOrganizationDao.getById(req.getCurrentOrgId());
                BUser servicePerson = bUserDao.getById(operatorUserId);
                FormStatusChangeMsgReq formStatusChangeMsgReq = new FormStatusChangeMsgReq()
                        .setServiceUnit(new FormStatusChangeMsgReq.Value(serviceOrg.getName()))
                        .setServicePerson(new FormStatusChangeMsgReq.Value(servicePerson.getRealname()))
                        .setStatus(new FormStatusChangeMsgReq.Value(status))
                        .setHandleTime(new FormStatusChangeMsgReq.Value(LocalDateTimeUtil.format(sendTime, DatePattern.NORM_DATETIME_FORMATTER)));
                SendSubscribeReq<FormStatusChangeMsgReq> sendSubscribeReq = new SendSubscribeReq<FormStatusChangeMsgReq>()
                        .setTemplateId(weChatAppletProperties.getOrderStatusChangeTemplate())
                        .setPage(weChatAppletProperties.getOrderStatusChangeTemplate())
                        .setToUserOpenId(customer.getOpenId())
                        .setData(formStatusChangeMsgReq);

                weChatAppletInfraService.sendSubscribe(sendSubscribeReq);
            } catch (Exception e) {
                log.error("sendFormStatusChangeMsg {} error: ", status, e);
            }
        });
    }

    @Async
    private void sendSubCompanyCustomerElinkMsg (WfForm wfForm, List<RoleUserResult> assignUsers, LocalDateTime now) {
        CompletableFuture.runAsync(() -> {
            try {
                final String msgTitle = activeProfile.contains("prod") ? "工单待处理通知" : "工单待处理通知（体验版）";
                JSONObject content = JSONUtil.parseObj(wfForm.getContent());
                StringBuilder msgContentBuilder = new StringBuilder();
                msgContentBuilder.append("工单名称：").append(content.get("businessName")).append("\\n");
                msgContentBuilder.append("工单编号：").append(content.get("code")).append("\\n");
                msgContentBuilder.append("派发时间：").append(now.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN))).append("\\n");
                msgContentBuilder.append("客户名称：").append(content.get("customerName")).append("\\n");
                msgContentBuilder.append("客户手机：").append(content.get("mobile")).append("\\n");

                for (RoleUserResult user : assignUsers) {
                    if (PhoneUtil.isPhone(user.getMobile())) {
                        String msgContent = elinkService.createElinkPushContent(IdUtil.fastSimpleUUID(), msgTitle, msgContentBuilder.toString(), user.getMobile());
                        log.info("==> 发送elink消息内容：{}", msgContent);
                        if (activeProfile.contains("dev")) {
                            log.info("<== 当前环境为dev，不发送elink消息");
                        } else {
                            String result = elinkService.pushElinkMsg(msgContent);
                            log.info("<== 发送elink消息响应：{}", result);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("sendElinkMsg formId {} error: ", wfForm.getFormId(), e);
            }
        });
    }

    @Async
    private void sendGroupCustomerElinkMsg(WfForm wfForm, List<String> phones, String msgTitle, String msgContent) {
        CompletableFuture.runAsync(() -> {
            try {
                //获取当前环境
                for (String phone : phones) {
                    if (PhoneUtil.isPhone(phone)) {
                        String msgBody = elinkService.createElinkPushContent(IdUtil.fastSimpleUUID(), msgTitle, msgContent, phone);
                        log.info("==> 发送elink消息内容：{}", msgBody);
                        if (activeProfile.contains("dev")) {
                            log.info("<== 当前环境为dev，不发送elink消息");
                        } else {
                            String result = elinkService.pushElinkMsg(msgBody);
                            log.info("<== 发送elink消息响应：{}", result);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("sendElinkMsg formId {} error: ", wfForm.getFormId(), e);
            }
        });

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
