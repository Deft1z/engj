package com.kge.energy.crm.pv.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PvDetailReq {
    @NotNull
    private Integer page;

    @NotNull
    private Integer size;

    private String startdate;
    private String enddate;
    private String zone;
    private String period;
}
