package com.kge.energy.crm.event.req;

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
@Schema(name = "上报埋点事件数据请求参数", description = "上报埋点事件数据请求参数")
public class ReportEventReq {

    @Schema(description = "事件键名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String eventKey;

    @Schema(description = "事件名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String eventName;

    @Schema(description = "事件类型（1-启动，2-登录，3-浏览，4-点击，5-搜索，6-分享）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer eventType;

    @Schema(description = "访问页面名称")
    private String viewPageName;

    @Schema(description = "访问页面地址")
    private String viewPageUrl;

    @Schema(description = "来源页面名称")
    private String sourcePageName;

    @Schema(description = "来源页面地址")
    private String sourcePageUrl;

    @Schema(description = "事件发生时间")
    private LocalDateTime eventTime;

    @Schema(description = "事件时长，单位毫秒")
    private Integer eventDuration;

    @Schema(description = "事件属性，json数据")
    private String eventProperty;
}
