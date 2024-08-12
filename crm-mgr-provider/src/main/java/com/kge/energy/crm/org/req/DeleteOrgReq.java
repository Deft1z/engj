package com.kge.energy.crm.org.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "删除组织name", description = "删除组织对象")
public class DeleteOrgReq {

    @Schema(description = "组织id")
    @NotNull
    private Integer organizationId;

}
