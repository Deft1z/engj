package com.kge.energy.crm.workorder.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "服务合同更新参数")
public class ServiceContractUpdateProjectTimeReq {

    @NotNull
    @Schema(description = "服务合同id")
    private Integer serviceContractId;

    @NotBlank
    @Schema(description = "项目时间")
    private String projectTime;

    @NotNull
    @Schema(description = "0 更新项目开始时间 1 更新项目结束时间")
    private Integer mode;
}
