package com.kge.energy.crm.role.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(name = "角色列表name", description = "角色列表对象")
public class RoleListReq {

    @Schema(description = "租户ID")
    private Integer tenantId;

    @Schema(description = "系统类型：applet、mgr", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String systemType;

}
