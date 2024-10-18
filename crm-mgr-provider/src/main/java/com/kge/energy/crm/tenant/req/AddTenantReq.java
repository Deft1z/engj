package com.kge.energy.crm.tenant.req;

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
@Schema(name = "新增租户name", description = "新增租户对象")
public class AddTenantReq {

    @Schema(description = "租户名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String name;

    @Schema(description = "联系人")
    private String contactName;

    @Schema(description = "联系人手机")
    private String contactMobile;

    @Schema(description = "租户状态（0正常 1停用）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer status;

}
