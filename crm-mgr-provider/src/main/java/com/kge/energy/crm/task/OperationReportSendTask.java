package com.kge.energy.crm.task;

import cn.hutool.core.date.DatePattern;
import com.kge.energy.crm.common.util.AppletLinkUtils;
import com.kge.energy.crm.enums.RoleEnums;
import com.kge.energy.crm.external.wechat.applet.service.WeChatAppletInfraService;
import com.kge.energy.crm.msg.MsgDomainService;
import com.kge.energy.crm.permission.service.DataPermissionDomainService;
import com.kge.energy.crm.user.service.UserDomainService;
import com.kge.energy.msg.dto.UserContactDto;
import com.kge.energy.msg.param.OperReportMsgToRoleParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 运营报告（周报、月报）定时发送任务
 * 每周一、月初发送消息短链给集团领导、业务公司领导、运营管理人
 *
 * @author zhengwenke
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class OperationReportSendTask {

    private final WeChatAppletInfraService weChatAppletInfraService;

    private final DataPermissionDomainService dataPermissionDomainService;

    private final UserDomainService userDomainService;

    private final MsgDomainService msgDomainService;

    /**
     * 每天早上9点执行周报或月报的消息发送任务
     */
    @Scheduled(cron = "${tmp.clean-cron:0 0 9 * * ?}")
    public synchronized void sendReportTask() {
        log.info("==> 执行运营周报、月报消息发送任务...");
        LocalDate now = LocalDate.now();
        String dimension;
        LocalDate startTime;
        LocalDate endTime;
        HashMap<String, String> urlMap = new HashMap<>();
        //每周一发送周报
        if (now.getDayOfWeek().getValue() == DayOfWeek.MONDAY.getValue()) {
            dimension = "week";
            startTime = now.minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            endTime = now.minusWeeks(1).with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
            send(urlMap, dimension, startTime, endTime);
        }
        //每月1号发送月报
        if (now.withDayOfMonth(1).equals(now)) {
            dimension = "month";
            startTime = now.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
            endTime = now.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
            send(urlMap, dimension, startTime, endTime);
        }
        log.info("<== 执行运营周报、月报消息发送任务完成");
    }

    private void send(HashMap<String, String> urlMap, String dimension, LocalDate startTime, LocalDate endTime) {
        Integer tenantId = 1;
        //日期格式化
        String startTimeStr = startTime.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
        String endTimeStr = endTime.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
        OperReportMsgToRoleParam msgParam = new OperReportMsgToRoleParam();
        //获取后台配置的数据权限
        List<RoleEnums> roleEnums = dataPermissionDomainService.getFunctionRoleEnums(tenantId, msgParam.getFunctionCode());
        if (!roleEnums.isEmpty()) {
            //集团领导、业务公司领导、运营管理人
            List<UserContactDto> configUsers = userDomainService.getUserContact(roleEnums, tenantId);
            //运营报告查收用户
            List<UserContactDto> limitUsers = userDomainService.getUserContact(Arrays.asList(RoleEnums.OP_REPORT_RX), tenantId);

            //缩小通知用户范围，限制运营报告查收用户，取configUsers和limitUsers的交集
            List<UserContactDto> notifyUsers = configUsers.stream()
                    .filter(user -> limitUsers.stream().map(UserContactDto::getMobile).toList().contains(user.getMobile()))
                    .toList();

            for (UserContactDto notifyUser : notifyUsers) {
                Integer orgId = notifyUser.getOrganizationId();
                //只通知有挂靠组织的用户
                if (orgId != null) {
                    //同一个组织的用户可重复使用相同的短链，减少生成次数
                    String url = urlMap.get(dimension + orgId);
                    if (StringUtils.isBlank(url)) {
                        url = weChatAppletInfraService.getWeChatAppletUrlLink(null, AppletLinkUtils.getOperationReportQuery(dimension, startTimeStr, endTimeStr, orgId), 30);
                        urlMap.put(dimension + orgId, url);
                    }
                    //发送消息
                    msgDomainService.sendCrmMsg(msgParam.setReportType(dimension)
                            .setStartTime(startTimeStr)
                            .setEndTime(endTimeStr)
                            .setPathUrl(url)
                            .setTenantId(tenantId)
                            .setNotifyUsers(Arrays.asList(notifyUser))
                    );
                }
            }
        }
    }

}