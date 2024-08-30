package com.kge.energy.crm.workflow.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.common.constans.ConstParam;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.RedisLockUtils;
import com.kge.energy.crm.common.util.RedisUtils;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.RoleEnums;
import com.kge.energy.crm.external.elink.ElinkService;
import com.kge.energy.crm.external.wechat.applet.property.WeChatAppletProperties;
import com.kge.energy.crm.external.wechat.applet.req.FormStatusChangeMsgReq;
import com.kge.energy.crm.external.wechat.applet.req.SendSubscribeReq;
import com.kge.energy.crm.external.wechat.applet.service.WeChatAppletInfraService;
import com.kge.energy.crm.repository.dao.*;
import com.kge.energy.crm.repository.entity.*;
import com.kge.energy.crm.repository.entityext.param.WorkOrderListParam;
import com.kge.energy.crm.repository.entityext.result.FlowResult;
import com.kge.energy.crm.repository.entityext.result.FormResult;
import com.kge.energy.crm.repository.entityext.result.RoleUserResult;
import com.kge.energy.crm.workflow.req.ConsultingAddReq;
import com.kge.energy.crm.workflow.req.ConsultingUpdateReq;
import com.kge.energy.crm.workflow.req.WfFormFlowReq;
import com.kge.energy.crm.workflow.req.WfFormReq;
import com.kge.energy.crm.workflow.resp.WfFormFlowResp;
import com.kge.energy.crm.workflow.resp.WfFormResp;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultingService {

    private static final String WORK_CODE_CACHE_KEY_PREFIX = "crm_order_code:";

    @Value("${spring.data.redis.front}")
    private String redisFront;

    private final Environment env;

    private final WeChatAppletProperties weChatAppletProperties;

    private final WfFormDao wfFormDao;

    private final WfFormFlowDao wfFormFlowDao;

    private final ScServiceContractDao scServiceContractDao;

    private final BUserDao bUserDao;

    private final BRoleDao bRoleDao;

    private final BOrganizationDao bOrganizationDao;

    private final ElinkService elinkService;

    private final WeChatAppletInfraService weChatAppletInfraService;

    private final RedisUtils redisUtils;

    private final RedisLockUtils redisLockUtils;

    /**
     * 创建业务工单
     *
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean save(ConsultingAddReq req) {
        LocalDateTime now = LocalDateTime.now();
        //生成工单编号
        String code = genOrderCode();

        ConsultingAddReq.ConsultingContent content = req.getContent();
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
        wfFormFlowDao.save(wfFormFlow);

        //todo 使用流程引擎替换现有的流程业务


        //发送消息，通知集团客服
        final String msgTitle = "工单待处理通知";
        StringBuilder msgContentBuilder = new StringBuilder();
        msgContentBuilder.append("工单名称：").append(content.getBusinessName()).append("\n");
        msgContentBuilder.append("所在地区：").append(content.getArea()).append("\n");
        msgContentBuilder.append("用电容量(kVA)：").append(content.getElectricityCapacity()).append("\n");
        msgContentBuilder.append("工单编号：").append(content.getCode()).append("\n");
        msgContentBuilder.append("生成时间：").append(now.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN))).append("\n");
        msgContentBuilder.append("客户名称：").append(content.getCustomerName()).append("\n");
        msgContentBuilder.append("客户手机：").append(content.getMobile()).append("\n");
        msgContentBuilder.append("备注：").append(req.getRemark());
        //获取集团客服人员手机号
        List<String> phones = bUserDao.findJtCustomerPhones(operator.getTenantId());
        sendElinkMsg(phones, msgTitle, msgContentBuilder.toString());

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

    public PageResp<WfFormResp> getByPage(WfFormReq req) {
        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();
        Assert.notNull(userInfoDto);

        IPage<WorkOrderListParam> reqIpage = new Page<>(req.getCurrentPage(), req.getPageSize());
        WorkOrderListParam workOrderListParam = BeanUtil.copyProperties(req, WorkOrderListParam.class);
        //限制数据查询范围，设置租户id
        workOrderListParam.setTenantId(UserInfoContextUtils.getCurrentTenantId());
        log.info("==> workOrderListParam= {}", workOrderListParam);

        IPage<FormResult> pages = wfFormDao.findListForWx(reqIpage, workOrderListParam, userInfoDto);
        List<WfFormResp> resps = BeanUtil.copyToList(pages.getRecords(), WfFormResp.class);

        return new PageResp<WfFormResp>()
                .setList(resps)
                .setCurrentPage(pages.getCurrent())
                .setPageSize(pages.getSize())
                .setTotal(pages.getTotal());
    }

    public List<WfFormFlowResp> getFlowByFormId(WfFormFlowReq req) {
        UserInfoDto userInfo = UserInfoContextUtils.getCurrentUserInfo();
        List<FlowResult> list = wfFormDao.getFlowByFormIdForWx(req.getFormId(), userInfo);
        if (list.isEmpty()) {
            throw new ServiceException("权限不足!");
        }
        return BeanUtil.copyToList(list, WfFormFlowResp.class);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean update(ConsultingUpdateReq req) {
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
            if (wfForm == null) {
                throw new ServiceException("工单不存在!");
            }
            //获取工单流转记录，时间倒序取最新的记录
            LambdaQueryWrapper<WfFormFlow> queryWrapper = Wrappers.<WfFormFlow>lambdaQuery()
                    .eq(WfFormFlow::getFormId, formId)
                    .eq(WfFormFlow::getTenantId, operator.getTenantId())
                    .orderByDesc(WfFormFlow::getCreateTime);
            List<WfFormFlow> flows = wfFormFlowDao.list(queryWrapper);
            WfFormFlow lastFlow = flows.get(0);

            //执行业务操作
            log.info("==> 开始执行业务操作, type={}...", req.getType());
            switch (req.getType()) {
                case 1:
                    //分派工单：集团客服分派工单给二级公司客服；form表status变为处理中，form表subStatus变为待处理；flow表新增记录status为流转二级公司处理；下一步由二级公司客服处理
                    assignOrder(req, wfForm, lastFlow.getActionType(), operator, now);
                    break;
                case 2:
                    //回复工单：二级公司客服处理工单，form表status和subStatus变为已处理；flow表新增记录status为已回复
                    handleOrder(req, wfForm, lastFlow.getActionType(), operator, now);
                    break;
                case 3:
                    //完成工单：form表status和subStatus变为已终止；flow表新增记录status为已完成; 已跟前端确认未使用
                    finishOrder(req, wfForm, lastFlow.getActionType(), operator, now);
                    break;
                case 4:
                    //终止工单：form表status和subStatus变为已终止；flow表新增记录status为已完成；关联合同的状态也会变为已终止
                    terminateOrder(req, wfForm, lastFlow.getActionType(), operator, now);
                    break;
                case 5:
                    //撤回工单：1、二级公司客服驳回给集团客服 2、集团客服撤回已到二级公司客服的工单，form表status和subStatus变为待处理；flow表新增记录status为流转集团处理；下一步由集团客服处理
                    withdrawOrder(req, wfForm, lastFlow.getActionType(), operator, now);
                    break;
                default:
                    return false;
            }
            log.info("<== 执行业务操作完成。");
        } finally {
            redisLockUtils.unlock(lockKey, requestId);
        }
        return true;
    }

    private void assignOrder(ConsultingUpdateReq req, WfForm wfForm, String lastFlowActionType, UserInfoDto operator, LocalDateTime now) {
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
                .setTenantId(operator.getTenantId());
        wfFormFlowDao.save(wfFormFlow);
        //发送elink消息通知，通知二级公司客服
        final String msgTitle = "工单待处理通知";
        JSONObject content = JSONUtil.parseObj(formContent);
        StringBuilder msgContentBuilder = new StringBuilder();
        msgContentBuilder.append("工单名称：").append(content.get("businessName")).append("\n");
        msgContentBuilder.append("工单编号：").append(content.get("code")).append("\n");
        msgContentBuilder.append("派发时间：").append(now.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN))).append("\n");
        msgContentBuilder.append("客户名称：").append(content.get("customerName")).append("\n");
        msgContentBuilder.append("客户手机：").append(content.get("mobile")).append("\n");
        String activeProfile = env.getProperty("spring.profiles.active");
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
    }

    private void handleOrder(ConsultingUpdateReq req, WfForm wfForm, String lastFlowActionType, UserInfoDto operator, LocalDateTime now) {
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
                .setTenantId(operator.getTenantId());
        wfFormFlowDao.save(wfFormFlow);
    }

    private void finishOrder(ConsultingUpdateReq req, WfForm wfForm, String lastFlowActionType, UserInfoDto operator, LocalDateTime now) {
        if (lastFlowActionType.equals(ConstParam.FlowFinished)) {
            throw new ServiceException("工单已经完成，不能重复完成!");
        }
        Long operatorUserId = operator.getUserId();
        Integer formId = wfForm.getFormId();
        Integer customerUserId = wfForm.getCreateUserId();
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
                .setTenantId(operator.getTenantId());
        wfFormFlowDao.save(wfFormFlow);

        //发送微信小程序消息，通知客户
        BUser customer = bUserDao.getById(customerUserId);
        //服务单位人员信息
        BOrganization serviceOrg = bOrganizationDao.getById(req.getCurrentOrgId());
        BUser servicePerson = bUserDao.getById(operatorUserId);
        FormStatusChangeMsgReq formStatusChangeMsgReq = new FormStatusChangeMsgReq()
                .setServiceUnit(new FormStatusChangeMsgReq.Value(serviceOrg.getName()))
                .setServicePerson(new FormStatusChangeMsgReq.Value(servicePerson.getRealname()))
                .setStatus(new FormStatusChangeMsgReq.Value(ConstParam.FlowFinished))
                .setHandleTime(new FormStatusChangeMsgReq.Value(now.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN))));
        SendSubscribeReq<FormStatusChangeMsgReq> sendSubscribeReq = new SendSubscribeReq<FormStatusChangeMsgReq>()
                .setTemplateId(weChatAppletProperties.getOrderStatusChangeTemplate())
                .setPage(weChatAppletProperties.getOrderStatusChangeTemplate())
                .setToUserOpenId(customer.getOpenId())
                .setData(formStatusChangeMsgReq);

        weChatAppletInfraService.sendSubscribe(sendSubscribeReq);
    }

    private void terminateOrder(ConsultingUpdateReq req, WfForm wfForm, String lastFlowActionType, UserInfoDto operator, LocalDateTime now) {
        if (lastFlowActionType.equals(ConstParam.FlowFinished)) {
            throw new ServiceException("工单已经完成或终止，不能重复完成或终止!");
        }
        Long operatorUserId = operator.getUserId();
        Integer currentRoleId = bRoleDao.getRoleIdByCode(operator.getRoleCodes().iterator().next(), operator.getTenantId());
        Integer formId = wfForm.getFormId();
        Integer customerUserId = wfForm.getCreateUserId();
        //终止工单
        LambdaUpdateWrapper<WfForm> wfUpdateWrapper = Wrappers.<WfForm>update().lambda()
                .set(WfForm::getStatus, ConstParam.Finished)
                .set(WfForm::getSubStatus, ConstParam.Finished)
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
                .setActionType(ConstParam.FlowFinished)
                .setActionContent(req.getContent())
                .setStatus(ConstParam.FlowFinished)
                .setCreateUserId(operatorUserId.intValue())
                .setSubStatus(req.getLevel().equals(1) ? ConstParam.FlowTagGroup : ConstParam.FlowTagSub)
                .setTenantId(operator.getTenantId());
        wfFormFlowDao.save(wfFormFlow);
        //终止合同
        LambdaUpdateWrapper<ScServiceContract> sscUpdateWrapper = Wrappers.<ScServiceContract>update().lambda()
                .set(ScServiceContract::getStatus, ConstParam.ContractDiscontinued)
                .eq(ScServiceContract::getFormId, formId)
                .and(i -> i.eq(ScServiceContract::getStatus, ConstParam.Ready).or().eq(ScServiceContract::getStatus, ConstParam.ContractUnderWay));
        scServiceContractDao.update(sscUpdateWrapper);

        //发送微信小程序消息，通知客户
        BUser customer = bUserDao.getById(customerUserId);
        //服务单位人员信息
        BOrganization serviceOrg = bOrganizationDao.getById(req.getCurrentOrgId());
        BUser servicePerson = bUserDao.getById(operatorUserId);
        FormStatusChangeMsgReq formStatusChangeMsgReq = new FormStatusChangeMsgReq()
                .setServiceUnit(new FormStatusChangeMsgReq.Value(serviceOrg.getName()))
                .setServicePerson(new FormStatusChangeMsgReq.Value(servicePerson.getRealname()))
                .setStatus(new FormStatusChangeMsgReq.Value(ConstParam.FlowFinished))
                .setHandleTime(new FormStatusChangeMsgReq.Value(now.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN))));
        SendSubscribeReq<FormStatusChangeMsgReq> sendSubscribeReq = new SendSubscribeReq<FormStatusChangeMsgReq>()
                .setTemplateId(weChatAppletProperties.getOrderStatusChangeTemplate())
                .setPage(weChatAppletProperties.getOrderStatusChangeTemplate())
                .setToUserOpenId(customer.getOpenId())
                .setData(formStatusChangeMsgReq);

        weChatAppletInfraService.sendSubscribe(sendSubscribeReq);
    }

    private void withdrawOrder(ConsultingUpdateReq req, WfForm wfForm, String lastFlowActionType, UserInfoDto operator, LocalDateTime now) {
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
                .setActionType(ConstParam.FlowGroupProcess)
                .setActionContent(req.getContent())
                .setStatus(ConstParam.FlowGroupProcess)
                .setCreateUserId(operatorUserId.intValue())
                .setTenantId(operator.getTenantId());
        wfFormFlowDao.save(wfFormFlow);

        //发送elink消息，通知集团客服
        final String msgTitle = "工单撤回通知";
        JSONObject content = JSONUtil.parseObj(formContent);
        StringBuilder msgContentBuilder = new StringBuilder();
        msgContentBuilder.append("工单名称：").append(content.get("businessName")).append("\n");
        msgContentBuilder.append("工单编号：").append(content.get("code")).append("\n");
        msgContentBuilder.append("撤回时间：").append(now.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN))).append("\n");
        msgContentBuilder.append("派发公司：").append(content.get("companyName")).append("\n");
        msgContentBuilder.append("撤回原因：").append(req.getContent());
        //获取集团客服人员手机号
        List<String> phones = bUserDao.findJtCustomerPhones(operator.getTenantId());
        sendElinkMsg(phones, msgTitle, msgContentBuilder.toString());

        //发送微信小程序消息，通知客户
        BUser customer = bUserDao.getById(customerUserId);
        //服务单位人员信息
        BOrganization serviceOrg = bOrganizationDao.getById(req.getCurrentOrgId());
        BUser servicePerson = bUserDao.getById(operatorUserId);
        FormStatusChangeMsgReq formStatusChangeMsgReq = new FormStatusChangeMsgReq()
                .setServiceUnit(new FormStatusChangeMsgReq.Value(serviceOrg.getName()))
                .setServicePerson(new FormStatusChangeMsgReq.Value(servicePerson.getRealname()))
                .setStatus(new FormStatusChangeMsgReq.Value(ConstParam.SendBack))
                .setHandleTime(new FormStatusChangeMsgReq.Value(now.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN))));
        SendSubscribeReq<FormStatusChangeMsgReq> sendSubscribeReq = new SendSubscribeReq<FormStatusChangeMsgReq>()
                .setTemplateId(weChatAppletProperties.getOrderStatusChangeTemplate())
                .setPage(weChatAppletProperties.getOrderStatusChangeTemplate())
                .setToUserOpenId(customer.getOpenId())
                .setData(formStatusChangeMsgReq);
        weChatAppletInfraService.sendSubscribe(sendSubscribeReq);

        //若集团客服撤回工单，需通知二级公司客服
        if (operator.getRoleCodes().contains(RoleEnums.JT_CUSTOMER.toString())) {
            msgContentBuilder = new StringBuilder();
            msgContentBuilder.append("工单名称：").append(content.get("businessName")).append("\n");
            msgContentBuilder.append("工单编号：").append(content.get("code")).append("\n");
            msgContentBuilder.append("撤回时间：").append(now.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN))).append("\n");
            msgContentBuilder.append("撤回人员：").append(RoleEnums.JT_CUSTOMER.getDesc()).append("\n");
            msgContentBuilder.append("撤回原因：").append(req.getContent());
            //获取二级公司客服人员手机号
            phones = bUserDao.findSubCustomerPhones(formCurrentOrgId, operator.getTenantId());
            sendElinkMsg(phones, msgTitle, msgContentBuilder.toString());
        }
    }

    private void sendElinkMsg(List<String> phones, String msgTitle, String msgContent) {
        //获取当前环境
        String activeProfile = env.getProperty("spring.profiles.active");
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
    }

}
