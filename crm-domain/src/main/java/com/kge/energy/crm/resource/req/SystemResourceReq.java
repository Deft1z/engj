package com.kge.energy.crm.resource.req;

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
@Schema(name = "系统菜单资源name", description = "系统菜单资源对象")
public class SystemResourceReq {

    @Schema(description = "系统类型：applet、mgr", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String systemType;

    @Schema(description = "系统类型：applet、mgr", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer tenantId;

}
