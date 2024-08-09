package com.kge.energy.crm.msg.req;

import com.kge.energy.crm.common.page.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(name = "用户消息列表请求参数", description = "用户消息列表请求参数")
public class UserMsgListReq extends PageReq {

    @Schema(description = "系统类型：applet、mgr", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String systemType;

    @Schema(description = "消息业务类型， 0 告警信息 1 工单通知 2 项目合同 3 投诉处理", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer msgBizType;

    @Schema(description = "告警级别: 一般告警、严重告警、紧急告警")
    @NotNull
    private String alarmLevel;

    @Schema(description = "开始时间 yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Schema(description = "结束时间 yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
