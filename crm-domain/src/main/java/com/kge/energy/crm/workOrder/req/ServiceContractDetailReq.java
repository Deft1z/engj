package com.kge.energy.crm.workOrder.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "服务合同详情查询参数")
public class ServiceContractDetailReq {
    @NotNull
    @Schema(description = "合同id")
    private Integer contractId;
}
