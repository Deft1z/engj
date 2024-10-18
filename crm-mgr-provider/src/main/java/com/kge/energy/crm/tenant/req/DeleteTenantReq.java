package com.kge.energy.crm.tenant.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(name = "删除租户name", description = "删除租户对象")
public class DeleteTenantReq {

    @Schema(description = "租户id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer id;

}
