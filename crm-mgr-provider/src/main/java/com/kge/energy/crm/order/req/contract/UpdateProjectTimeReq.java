package com.kge.energy.crm.order.req.contract;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateProjectTimeReq {

    private String projectTime;
    private Integer mode;

    @NotNull
    private Integer serviceContractId;

}
