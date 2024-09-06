package com.kge.energy.crm.contract.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
@Schema(description = "服务合同更新参数")
public class ScServiceContractProjTimeUpdReq {

    @NotNull
    @Schema(description = "服务合同id")
    private Integer serviceContractId;

    @NotNull
    @Schema(description = "项目时间")
    private LocalDate projectTime;

    @NotNull
    @Schema(description = "0 更新项目开始时间 1 更新项目结束时间")
    private Integer mode;
}