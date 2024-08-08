package com.kge.energy.crm.pv.req;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PvInfoReq {
    private String startdate;
    private String enddate;
}
