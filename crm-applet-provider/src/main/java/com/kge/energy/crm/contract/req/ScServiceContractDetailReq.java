package com.kge.energy.crm.contract.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "服务合同查询参数")
public class ScServiceContractDetailReq {

    @NotNull
    @Schema(description = "工单id")
    private Integer formId;

}