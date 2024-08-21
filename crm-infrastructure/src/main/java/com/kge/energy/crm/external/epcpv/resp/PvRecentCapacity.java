package com.kge.energy.crm.external.epcpv.resp;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PvRecentCapacity {

    private String week;
    private String month;
    private String year;

}
