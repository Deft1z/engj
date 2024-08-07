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
@Schema(name = "编辑角色name", description = "编辑角色对象")
public class UpdateRoleReq {

    @Schema(description = "角色ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer roleId;

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
