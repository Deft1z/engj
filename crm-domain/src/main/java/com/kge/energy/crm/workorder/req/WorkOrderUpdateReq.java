package com.kge.energy.crm.workorder.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "业务工单更新参数")
public class WorkOrderUpdateReq {
    @NotNull
    @Schema(description = "工单id")
    private Integer formId;

    @Schema(description = "工单内容")
    private String content;

    @NotNull
    @Schema(description = "类型 1 分派工单 2 回复工单 3 完成工单 4 终止工单 5 撤回工单")
    private Integer type;

    @NotNull
    @Schema(description = "级别 1 集团 2 二级公司")
    private Integer level;

    @NotNull
    @Schema(description = "当前组织id")
    private Integer currentOrgId;

    @NotNull
    @Schema(description = "当前角色id")
    private Integer currentRoleId;
}
