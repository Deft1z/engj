package com.kge.energy.crm.task;

import cn.hutool.core.date.DatePattern;
import com.kge.energy.crm.common.constans.ConstParam;
import com.kge.energy.crm.common.util.AppletLinkUtils;
import com.kge.energy.crm.enums.RoleEnums;
import com.kge.energy.crm.external.wechat.applet.service.WeChatAppletInfraService;
import com.kge.energy.crm.msg.MsgDomainService;
import com.kge.energy.crm.permission.service.DataPermissionDomainService;
import com.kge.energy.crm.repository.dao.WfFormDao;
import com.kge.energy.crm.repository.entityext.result.TimeoutFormResult;
import com.kge.energy.crm.user.service.UserDomainService;
import com.kge.energy.msg.dto.UserContactDto;
import com.kge.energy.msg.param.AssignTimeoutMsgToRoleParam;
import com.kge.energy.msg.param.HandleTimeoutMsgToRoleParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * 业务工单超时提醒任务
 * 集团未分派超过24小时、二级公司未处理超过24小时则发送催办通知；
 *
 * @author zhengwenke
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class BizOrderTimeoutNotifyTask {

    /**
     * 工单超时阈值（分钟）-集团客服
     */
    @Value("${biz-order.timeout-notify.jt-threshold-min:1440}")
    private Long jtThresholdMin;

    /**
     * 工单超时提醒间隔（分钟）-集团客服
     */
    @Value("${biz-order.timeout-notify.jt-interval-min:120}")
    private Long jtIntervalMin;

    /**
     * 工单超时提醒持续时长（分钟）-集团客服
     */
    @Value("${biz-order.timeout-notify.jt-duration-min:360}")
    private Long jtDurationMin;

    /**
     * 工单超时阈值（分钟）-二级公司客服
     */
    @Value("${biz-order.timeout-notify.sc-threshold-min:1440}")
    private Long scThresholdMin;

    /**
     * 工单超时提醒间隔（分钟）-二级公司客服
     */
    @Value("${biz-order.timeout-notify.sc-interval-min:120}")
    private Long scIntervalMin;

    /**
     * 工单超时提醒持续时长（分钟）-二级公司客服
     */
    @Value("${biz-order.timeout-notify.sc-duration-min:360}")
    private Long scDurationMin;


    private final MsgDomainService msgDomainService;

    private final DataPermissionDomainService dataPermissionDomainService;

    private final UserDomainService userDomainService;

    private final WeChatAppletInfraService weChatAppletInfraService;

    private final WfFormDao wfFormDao;

    /**
     * 提醒集团客服和二级公司客服工单超时待处理
     */
    @Scheduled(cron = "${biz-order.timeout-notify.exec-cron:0 0 */1 * * ?}")
    public void notifyCustomer() {
        log.debug("==> 执行业务工单超时提醒任务...");
        notifyAssignTimeout();
        notifyHandleTimeout();
        log.debug("<== 执行业务工单超时提醒任务完成");
    }

    private void notifyAssignTimeout() {
        LocalDateTime now = LocalDateTime.now();
        List<String> notifyStatus = Arrays.asList(ConstParam.FlowStart, ConstParam.FlowGroupWithdraw, ConstParam.FlowCompanyReturn);
        String startTime = now.minusMinutes(jtThresholdMin + jtDurationMin).format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN));
        String endTime = now.minusMinutes(jtThresholdMin).format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN));
        //查询待集团客服分派的超时工单
        AssignTimeoutMsgToRoleParam msgParam = new AssignTimeoutMsgToRoleParam();
        List<TimeoutFormResult> timeoutNotifyForms = wfFormDao.getTimeoutNotifyForms(notifyStatus, startTime, endTime, msgParam.getMsgTemplateCode());
        for (TimeoutFormResult form : timeoutNotifyForms) {
            //发送消息，通知集团客服
            List<RoleEnums> roleEnums = dataPermissionDomainService.getFunctionRoleEnums(form.getTenantId(), msgParam.getFunctionCode());
            if (!roleEnums.isEmpty()) {
                List<UserContactDto> userContact = userDomainService.getUserContact(roleEnums, form.getTenantId());
                //当前时间预留1分钟与最新的消息通知时间比较，以抵消发送耗时
                if (form.getLastNotifyTime() == null || now.plusMinutes(1).minusMinutes(jtIntervalMin).isAfter(form.getLastNotifyTime())) {
                    msgDomainService.sendCrmMsg(msgParam.setOrderName(form.getOrderName())
                            .setOrderCode(form.getOrderCode())
                            .setFlowTime(form.getFlowTime().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)))
                            .setCustomerName(form.getCustomerName())
                            .setRemark(form.getActionContent())
                            .setThresholdHour((int) Duration.between(form.getFlowTime(), now).toHours())
                            .setPathUrl(weChatAppletInfraService.getWeChatAppletUrlLink(null, AppletLinkUtils.getFormDetailQuery(form.getFormId()), 30))
                            .setTenantId(form.getTenantId())
                            .setNotifyUsers(userContact)
                            .setMsgBizId(form.getFormId())
                    );
                }
            }
        }
    }

    private void notifyHandleTimeout() {
        LocalDateTime now = LocalDateTime.now();
        List<String> notifyStatus = Arrays.asList(ConstParam.FlowCompanyProcess);
        String startTime = now.minusMinutes(scThresholdMin + scDurationMin).format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN));
        String endTime = now.minusMinutes(scThresholdMin).format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN));
        //查询待公司二级客服处理的超时工单
        HandleTimeoutMsgToRoleParam msgParam = new HandleTimeoutMsgToRoleParam();
        List<TimeoutFormResult> timeoutNotifyForms = wfFormDao.getTimeoutNotifyForms(notifyStatus, startTime, endTime, msgParam.getMsgTemplateCode());
        for (TimeoutFormResult form : timeoutNotifyForms) {
            Integer tenantId = form.getTenantId();
            //发送消息，通知二级公司客服
            List<RoleEnums> roleEnums = dataPermissionDomainService.getFunctionRoleEnums(tenantId, msgParam.getFunctionCode());
            if (!roleEnums.isEmpty()) {
                List<UserContactDto> userContact = userDomainService.getUserContact(roleEnums, form.getCurrentOrgId(), tenantId);
                //当前时间预留1分钟与最新的消息通知时间比较，以抵消发送耗时
                if (form.getLastNotifyTime() == null || now.plusMinutes(1).minusMinutes(scIntervalMin).isAfter(form.getLastNotifyTime())) {
                    msgDomainService.sendCrmMsg(msgParam.setOrderName(form.getOrderName())
                            .setOrderCode(form.getOrderCode())
                            .setFlowTime(form.getFlowTime().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)))
                            .setCustomerName(form.getCustomerName())
                            .setRemark(form.getActionContent())
                            .setThresholdHour((int) Duration.between(form.getFlowTime(), now).toHours())
                            .setPathUrl(weChatAppletInfraService.getWeChatAppletUrlLink(null, AppletLinkUtils.getFormDetailQuery(form.getFormId()), 30))
                            .setTenantId(tenantId)
                            .setNotifyUsers(userContact)
                            .setMsgBizId(form.getFormId())
                    );
                }
            }
        }
    }

}