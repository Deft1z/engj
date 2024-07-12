package com.kge.energy.crm.dashboard.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DashBoardContractOrderReq extends DashBoardReq{
    @JsonProperty("CompanyId")
    private Integer companyId;

    @JsonProperty("Area")
    private String area;

    @NotNull
    @JsonProperty("Mode")
    private Integer mode;
}
