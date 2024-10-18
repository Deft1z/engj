package com.kge.energy.crm.repository.entityext.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "租户列表name", description = "租户列表对象")
public class TenantListForOrgResult {

    @Schema(description = "租户id")
    private Integer id;

    @Schema(description = "租户名")
    private String name;

}
