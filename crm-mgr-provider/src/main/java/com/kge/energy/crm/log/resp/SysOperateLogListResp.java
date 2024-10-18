package com.kge.energy.crm.log.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(name = "操作日志列表响应参数", description = "操作日志列表响应参数")
public class SysOperateLogListResp {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "链路ID")
    private String traceId;

    @Schema(description = "租户名")
    private String tenantName;

    @Schema(description = "操作人ID")
    private Integer operatorId;

    @Schema(description = "操作人名称")
    private String operatorName;

    @Schema(description = "操作时间")
    private LocalDateTime operateTime;

    @Schema(description = "操作模块")
    private String operateModule;

    @Schema(description = "操作行为")
    private String operateBehavior;
}
