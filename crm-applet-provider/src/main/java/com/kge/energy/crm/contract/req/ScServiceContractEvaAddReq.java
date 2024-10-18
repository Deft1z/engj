package com.kge.energy.crm.contract.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "服务合同评价新增参数")
public class ScServiceContractEvaAddReq {

    @NotNull
    @Schema(description = "服务合同id")
    private Integer serviceContractId;

    @NotNull
    @Schema(description = "满意度")
    private Integer satisfaction;

    @Schema(description = "评价内容")
    private String evaluate;

}