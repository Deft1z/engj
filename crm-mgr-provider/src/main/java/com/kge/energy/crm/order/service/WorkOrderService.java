package com.kge.energy.crm.order.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.common.constans.ConstParam;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.AuthVerifyUtils;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.RoleEnums;
import com.kge.energy.crm.external.wechat.applet.property.WeChatAppletProperties;
import com.kge.energy.crm.external.wechat.applet.req.FormStatusChangeMsgReq;
import com.kge.energy.crm.external.wechat.applet.req.SendSubscribeReq;
import com.kge.energy.crm.external.wechat.applet.service.WeChatAppletInfraService;
import com.kge.energy.crm.flow.service.WfFormFlowService;
import com.kge.energy.crm.order.req.GetFlowByFormIdReq;
import com.kge.energy.crm.order.req.WorkOrdeUpdateReq;
import com.kge.energy.crm.order.req.WorkOrderListReq;
import com.kge.energy.crm.order.req.WxUserWorkOrderReq;
import com.kge.energy.crm.order.resp.FlowResp;
import com.kge.energy.crm.order.resp.FormResp;
import com.kge.energy.crm.repository.dao.*;
import com.kge.energy.crm.repository.entity.*;
import com.kge.energy.crm.repository.entityext.param.WorkOrderListParam;
import com.kge.energy.crm.repository.entityext.param.WxUserWorkOrderParam;
import com.kge.energy.crm.repository.entityext.result.FlowResult;
import com.kge.energy.crm.repository.entityext.result.FormResult;
import com.kge.energy.crm.repository.entityext.result.RoleUserResult;
import com.kge.platform.framework.common.exception.ServiceException;
import com.kge.platform.framework.common.net.CommonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkOrderService {

    private final WfFormFlowService wfFormFlowService;

    private final BUserDao bUserDao;

    private final WfFormDao wfFormDao;

    private final WfFormFlowDao wfFormFlowDao;

    private final ScServiceContractDao scServiceContractDao;

    private final BOrganizationDao bOrganizationDao;

    private final WeChatAppletProperties weChatAppletProperties;

    private final WeChatAppletInfraService weChatAppletInfraService;


    /**
     * 工单列表
     */
//    public PageResp<FormResp> list(WorkOrderListReq req) {
//        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();
//        Assert.notNull(userInfoDto);
//
//        //数据权限校验，超级管理员可查询全部租户数据，非超管默认只能查询同一租户下的数据
//        if (AuthVerifyUtils.notSuperAdmin() && ObjUtil.isNull(req.getTenantId())) {
//            req.setTenantId(userInfoDto.getTenantId());
//        }
//        if (AuthVerifyUtils.notSuperAdmin() && ObjUtil.notEqual(userInfoDto.getTenantId(), req.getTenantId())) {
//            throw new ServiceException("非法请求，不允许查看其他租户信息");
//        }
//
//        IPage<WorkOrderListParam> reqIpage = new Page<>(req.getCurrentPage(), req.getPageSize());
//        WorkOrderListParam workOrderListParam = BeanUtil.copyProperties(req, WorkOrderListParam.class);
//
//        IPage<FormResult> pages = wfFormDao.findListForWx(reqIpage, workOrderListParam, userInfoDto);
//        List<FormResp> resps = BeanUtil.copyToList(pages.getRecords(), FormResp.class);
//
//        return new PageResp<FormResp>()
//                .setList(resps)
//                .setCurrentPage(pages.getCurrent())
//                .setPageSize(pages.getSize())
//                .setTotal(pages.getTotal());
//    }

    /**
     * 分派工单 终止工单 处理工单
     * 暂时不重构，待上流程引擎
     */
    @Transactional
    public CommonResult<Object> workOrderUpdate(WorkOrdeUpdateReq req) {

        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();

        return switch (req.getType()) {
            //流转工单（工单分配）
            case 1 -> assignOrder(req, userInfoDto);

            //回复工单（工单处理）
            case 2 -> handleOrder(req, userInfoDto);

            //完成工单
            case 3 -> finishOrder(req, userInfoDto);

            //终止工单
            case 4 -> terminateOrder(req, userInfoDto);

            //撤回工单
            case 5 -> withdrawOrder(req, userInfoDto);

            default -> CommonResult.suc(false);
        };

    }


    /**
     * 微信小程序客户 -> 工单
     */
    public PageResp<FormResp> getWxUserOrder(WxUserWorkOrderReq req) {
        IPage<WxUserWorkOrderParam> reqIpage = new Page<>(req.getCurrentPage(), req.getPageSize());
        WxUserWorkOrderParam wxUserWorkOrderParam = BeanUtil.copyProperties(req, WxUserWorkOrderParam.class);
        IPage<FormResult> pages = wfFormFlowService.getWxUserWorkOrder(reqIpage, wxUserWorkOrderParam);
        List<FormResp> resps = BeanUtil.copyToList(pages.getRecords(), FormResp.class);

        return new PageResp<FormResp>()
                .setList(resps)
                .setCurrentPage(pages.getCurrent())
                .setPageSize(pages.getSize())
                .setTotal(pages.getTotal());
    }

    private CommonResult<Object> assignOrder(WorkOrdeUpdateReq req, UserInfoDto userInfoDto) {
        //检查工单是否已经流转 (工单分配)
        List<WfFormFlow> fms = wfFormFlowService.selectFlowByFormIdAndActionType(req.getFormId(), ConstParam.FlowCompanyProcess);
        if (CollectionUtil.isNotEmpty(fms)) {
            return CommonResult.suc(4000);
        }
        //流转工单
        //查询所选组织是否有客服角色
        List<RoleUserResult> assignUsers = bUserDao.getUserByRoleCodeAndOrgId(RoleEnums.SUB_COMPANY_CUSTOMER.getCode(), req.getCurrentOrgId(), userInfoDto.getTenantId());
        if (assignUsers.isEmpty()) {
            throw new ServiceException("没有客服角色!");
        }

        WfForm form = wfFormDao.getById(req.getFormId());
        form.setStatus(ConstParam.Processing)
                .setSubStatus(ConstParam.WaitingForProcessing)
                .setTimeReception(LocalDateTime.now())
                .setModifyUserId(Math.toIntExact(userInfoDto.getUserId()))
                .setCurrentOrgId(req.getCurrentOrgId())
                .setCurrentRoleId(req.getCurrentRoleId())
                .setTenantId(userInfoDto.getTenantId());
        wfFormDao.updateById(form);

        WfFormFlow wfFormFlow = new WfFormFlow()
                .setFormId(req.getFormId())
                .setTimeAction(LocalDateTime.now())
                .setActionType(ConstParam.FlowCompanyProcess)
                .setActionContent(req.getContent())
                .setStatus(ConstParam.FlowCompanyProcess)
                .setCreateUserId(Math.toIntExact(userInfoDto.getUserId()))
                .setTenantId(userInfoDto.getTenantId());
        if (ObjUtil.equals(req.getLevel(), 1)) {
            wfFormFlow.setStatus(ConstParam.FlowTagGroup);
        } else {
            wfFormFlow.setStatus(ConstParam.FlowTagSub);
        }
        wfFormFlowDao.save(wfFormFlow);
        return CommonResult.suc(true);
    }

    private CommonResult<Object> handleOrder(WorkOrdeUpdateReq req, UserInfoDto userInfoDto) {
        LocalDateTime now = LocalDateTime.now();

        WfForm form = wfFormDao.getById(req.getFormId());
        form.setStatus(ConstParam.Processed)
                .setSubStatus(ConstParam.Processed)
                .setTimeReception(now)
                .setModifyUserId(userInfoDto.getUserId().intValue());
        if (!AuthVerifyUtils.isSuperAdmin()) {
            form.setCurrentOrgId(req.getCurrentOrgId()).setCurrentRoleId(req.getCurrentRoleId());
        }
        wfFormDao.updateById(form);

        WfFormFlow wfFormFlow = new WfFormFlow().setFormId(req.getFormId())
                .setUserId(userInfoDto.getUserId().intValue())
                .setTimeAction(now)
                .setActionType(ConstParam.FlowHasFeedback)
                .setActionContent(req.getContent())
                .setStatus(ConstParam.FlowHasFeedback)
                .setCreateUserId(userInfoDto.getUserId().intValue())
                .setSubStatus(req.getLevel() == 1 ? ConstParam.FlowTagGroup : ConstParam.FlowTagSub)
                .setTenantId(userInfoDto.getTenantId());
        wfFormFlowDao.save(wfFormFlow);

        //发送微信小程序消息，通知客户
        sendFormStatusChangeMsg(req, userInfoDto, form, now, ConstParam.FlowHasFeedback);

        return CommonResult.suc(true);
    }

    private CommonResult<Object> finishOrder(WorkOrdeUpdateReq req, UserInfoDto userInfoDto) {
        LocalDateTime now = LocalDateTime.now();

        //检查工单是否已经完成
        LambdaQueryWrapper<WfFormFlow> queryWrapper = Wrappers.<WfFormFlow>lambdaQuery()
                .eq(WfFormFlow::getFormId, req.getFormId())
                .eq(WfFormFlow::getActionType, ConstParam.FlowFinished);
        long count = wfFormFlowDao.count(queryWrapper);
        if (count > 0) {
            return CommonResult.suc(4002);
        }

        // 完成工单
        WfForm form = wfFormDao.getById(req.getFormId());
        form.setStatus(ConstParam.Finished)
                .setSubStatus(ConstParam.Finished)
                .setTimeFinished(form.getTimeFinished())
                .setModifyUserId(userInfoDto.getUserId().intValue())
                .setCurrentOrgId(req.getCurrentOrgId())
                .setCurrentRoleId(req.getCurrentRoleId());
        wfFormDao.updateById(form);

        //新增流转记录
        WfFormFlow wfFormFlow = new WfFormFlow().setFormId(req.getFormId())
                .setUserId(userInfoDto.getUserId().intValue())
                .setTimeAction(now)
                .setActionType(ConstParam.FlowFinished)
                .setActionContent(req.getContent())
                .setStatus(ConstParam.FlowFinished)
                .setCreateUserId(userInfoDto.getUserId().intValue())
                .setSubStatus(req.getLevel() == 1 ? ConstParam.FlowTagGroup : ConstParam.FlowTagSub)
                .setTenantId(userInfoDto.getTenantId());
        wfFormFlowDao.save(wfFormFlow);

        //发送微信小程序消息，通知客户
        sendFormStatusChangeMsg(req, userInfoDto, form, now, ConstParam.FlowFinished);

        return CommonResult.suc(true);
    }

    private CommonResult<Object> terminateOrder(WorkOrdeUpdateReq req, UserInfoDto userInfoDto) {
        LocalDateTime now = LocalDateTime.now();

        //检查工单是否已经终止 （工单终止）
        LambdaQueryWrapper<WfFormFlow> queryWrapper = Wrappers.<WfFormFlow>lambdaQuery()
                .eq(WfFormFlow::getFormId, req.getFormId())
                .eq(WfFormFlow::getActionType, ConstParam.FlowFinished);
        long count = wfFormFlowDao.count(queryWrapper);
        if (count > 0) {
            return CommonResult.suc(4003);
        }

        // 终止工单
        WfForm form = wfFormDao.getById(req.getFormId());
        form.setStatus(ConstParam.Finished)
                .setSubStatus(ConstParam.Finished)
                .setTimeFinished(form.getTimeFinished())
                .setModifyUserId(userInfoDto.getUserId().intValue())
                .setCurrentOrgId(req.getCurrentOrgId())
                .setCurrentRoleId(req.getCurrentRoleId());
        wfFormDao.updateById(form);

        //新增流转记录
        WfFormFlow wfFormFlow = new WfFormFlow()
                .setFormId(req.getFormId())
                .setUserId(userInfoDto.getUserId().intValue())
                .setTimeAction(now)
                .setActionType(ConstParam.FlowFinished)
                .setActionContent(req.getContent())
                .setStatus(ConstParam.FlowFinished)
                .setCreateUserId(userInfoDto.getUserId().intValue())
                .setSubStatus(req.getLevel() == 1 ? ConstParam.FlowTagGroup : ConstParam.FlowTagSub)
                .setTenantId(userInfoDto.getTenantId());
        wfFormFlowDao.save(wfFormFlow);

        //终止合同
        LambdaUpdateWrapper<ScServiceContract> sscUpdateWrapper = Wrappers.<ScServiceContract>update().lambda()
                .set(ScServiceContract::getStatus, ConstParam.ContractDiscontinued)
                .eq(ScServiceContract::getFormId, req.getFormId())
                .and(i -> i.eq(ScServiceContract::getStatus, ConstParam.ContractNotBegin).or().eq(ScServiceContract::getStatus, ConstParam.ContractUnderWay));
        scServiceContractDao.update(sscUpdateWrapper);

        //发送微信小程序消息，通知客户
        sendFormStatusChangeMsg(req, userInfoDto, form, now, ConstParam.Finished);

        return CommonResult.suc(true);
    }

    private CommonResult<Object> withdrawOrder(WorkOrdeUpdateReq req, UserInfoDto userInfoDto) {
        LocalDateTime now = LocalDateTime.now();

        //检查工单是否已经撤回
        LambdaQueryWrapper<WfFormFlow> queryWrapper = Wrappers.<WfFormFlow>lambdaQuery()
                .eq(WfFormFlow::getFormId, req.getFormId())
                .eq(WfFormFlow::getActionType, ConstParam.FlowGroupProcess);
        long count = wfFormFlowDao.count(queryWrapper);
        if (count > 0) {
            return CommonResult.suc(4004);
        }

        // 撤回工单
        WfForm form = wfFormDao.getById(req.getFormId());
        form.setStatus(ConstParam.WaitingForProcessing)
                .setSubStatus(ConstParam.WaitingForProcessing)
                .setModifyUserId(userInfoDto.getUserId().intValue())
                .setCurrentOrgId(1)
                .setCurrentRoleId(2);
        wfFormDao.updateById(form);

        //新增流转记录
        WfFormFlow wfFormFlow = new WfFormFlow().setFormId(req.getFormId())
                .setUserId(userInfoDto.getUserId().intValue())
                .setTimeAction(now)
                .setActionType(ConstParam.FlowGroupProcess)
                .setActionContent(req.getContent())
                .setStatus(ConstParam.FlowGroupProcess)
                .setCreateUserId(userInfoDto.getUserId().intValue())
                .setSubStatus(ConstParam.FlowTagGroup)
                .setTenantId(userInfoDto.getTenantId());
        wfFormFlowDao.save(wfFormFlow);

        //发送微信小程序消息，通知客户
        sendFormStatusChangeMsg(req, userInfoDto, form, now, ConstParam.SendBack);

        return CommonResult.suc(true);
    }

    @Async
    private void sendFormStatusChangeMsg(WorkOrdeUpdateReq req, UserInfoDto userInfoDto, WfForm form, LocalDateTime sendTime, String status) {
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
}
