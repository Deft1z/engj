package com.kge.energy.crm.permission.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(name = "更新数据权限配置", description = "更新数据权限配置对象")
public class UpdateDataPermissionReq {

    @Schema(description = "配置ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer id;

    @Schema(description = "业务功能配置ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer bizFunctionId;

    @Schema(description = "角色ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer roleId;

    @Schema(description = "数据权限范围类型：0-所有，1-租户，2-集团，3-公司，4-部门，5-个人", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer dataRangeType;

    @Schema(description = "优先级，越大越高")
    private Integer priority;

}
