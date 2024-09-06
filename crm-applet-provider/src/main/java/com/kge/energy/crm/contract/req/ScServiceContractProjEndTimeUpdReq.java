package com.kge.energy.crm.contract.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@Schema(description = "服务合同更新参数")
public class ScServiceContractProjEndTimeUpdReq {

    @NotNull
    @Schema(description = "服务合同id")
    private Integer serviceContractId;

    @NotNull
    @Schema(description = "项目结束时间")
    private LocalDateTime projectEndTime;


}