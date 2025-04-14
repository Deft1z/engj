package com.kge.energy.crm.operation.dashboard.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class OperationDashboardReq {

    @Schema(description = "统计维度：week|month")
    @Pattern(regexp = "week|month", message = "统计维度只能是week或month")
    private String dimension;

    @Schema(description = "开始时间 yyyy-MM-dd")
    private LocalDate startTime;

    @Schema(description = "结束时间 yyyy-MM-dd")
    private LocalDate endTime;

    @Schema(description = "组织ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer orgId;

    @Schema(description = "导出类型：0 excel 1 pdf，仅用于数据导出接口")
    private Integer exportType = 0;

    @JsonIgnore
    private List<String> statDims;

}
