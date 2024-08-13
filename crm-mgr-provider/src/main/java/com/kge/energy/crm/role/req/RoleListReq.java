package com.kge.energy.crm.role.req;

import com.kge.energy.crm.common.page.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(name = "角色列表请求参数", description = "角色列表请求参数")
public class RoleListReq extends PageReq {

    @Schema(description = "租户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer tenantId;

    @Schema(description = "系统类型：applet、mgr", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Pattern(regexp = "applet|mgr")
    private String systemType;

}
