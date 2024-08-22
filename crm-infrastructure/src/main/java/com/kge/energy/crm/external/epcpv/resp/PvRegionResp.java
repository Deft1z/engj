package com.kge.energy.crm.external.epcpv.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class PvRegionResp {
    private String name;
    private String projectnum;
    private String capacity;
    private String capacityPer;
    private String projectper;
    private String owncapacity;
    private String owncapacityper;
}
