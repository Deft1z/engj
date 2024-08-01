package com.kge.energy.crm.external.epcpv.req;

import lombok.Data;

@Data
public class EpcpvDetailsCondition {
    private String queryDateStart;
    private String queryDateEnd;
    private String regionName;
    private String stageShowName;
}
