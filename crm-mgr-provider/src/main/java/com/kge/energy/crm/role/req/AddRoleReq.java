package com.kge.energy.crm.role.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(name = "新增角色name", description = "新增角色对象")
public class AddRoleReq {

    @Schema(description = "租户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer tenantId;

    @Schema(description = "系统类型：applet、mgr", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String systemType;

    @Schema(description = "角色名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String name;

    @Schema(description = "角色编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String code;

    @Schema(description = "角色状态（0正常 1停用）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer status;

    @Schema(description = "备注")
    private String remark;

}
