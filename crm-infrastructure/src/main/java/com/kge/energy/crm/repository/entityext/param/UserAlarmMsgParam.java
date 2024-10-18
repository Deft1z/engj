package com.kge.energy.crm.repository.entityext.param;

import com.kge.energy.crm.common.page.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class UserAlarmMsgParam extends PageReq {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "角色编码")
    private Set<String> roleCodes = new HashSet<>();

    @Schema(description = "消息业务类型， 0 告警信息 1 工单通知 2 项目合同 3 投诉处理", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer msgBizType = 0;

    @Schema(description = "告警级别: 一般告警、严重告警、紧急告警")
    private String alarmLevel;

    @Schema(description = "工单流转: 工单生成、工单分派、工单退回、工单撤回、工单终止、工单处理、工单完成")
    private String functionName;

    @Schema(description = "开始时间 yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Schema(description = "结束时间 yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @Schema(description = "已读标识，false 未读 true 已读")
    private Boolean isRead;

    @Schema(description = "false 查看全部消息 true 仅查看个人消息")
    private Boolean onlyMe;

}
