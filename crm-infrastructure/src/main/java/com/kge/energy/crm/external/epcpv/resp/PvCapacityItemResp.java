package com.kge.energy.crm.external.epcpv.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class PvCapacityItemResp {
    private String name;
    private String value;
    private String per;
}
