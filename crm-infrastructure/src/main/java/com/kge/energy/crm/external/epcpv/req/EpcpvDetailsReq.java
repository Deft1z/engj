package com.kge.energy.crm.external.epcpv.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EpcpvDetailsReq {

    @NotNull
    private Integer pageNo;

    @NotNull
    private Integer pageSize;

    private EpcpvDetailsCondition condition;

}
