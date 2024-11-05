package com.kge.energy.crm.operation.dashboard.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class OperationDashboardReq {

    @Schema(description = "开始时间 yyyy-MM-dd")
    private LocalDate startTime;

    @Schema(description = "结束时间 yyyy-MM-dd")
    private LocalDate endTime;

    @Schema(description = "组织ID")
    private Integer orgId;
}
