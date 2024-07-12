package com.kge.energy.crm.dashboard.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DashBoardContractOrderReq extends DashBoardReq{
    private Integer companyId;

    private String area;

    @NotNull
    private Integer mode;
}
