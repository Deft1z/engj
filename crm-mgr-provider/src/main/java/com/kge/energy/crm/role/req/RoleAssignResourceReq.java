package com.kge.energy.crm.role.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(name = "给角色关联菜单请求参数", description = "给角色关联菜单请求参数")
public class RoleAssignResourceReq {

    @Schema(description = "角色ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer roleId;

    @Schema(description = "系统类型：applet、mgr", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Pattern(regexp = "applet|mgr")
    private String systemType;

    @Schema(description = "菜单资源ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Set<Integer> resourceIds;
}
