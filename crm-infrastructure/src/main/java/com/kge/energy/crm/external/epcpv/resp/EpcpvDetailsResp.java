package com.kge.energy.crm.external.epcpv.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpcpvDetailsResp {
    private String name;
    private String zone;
    private String period;
    private String user;
    private String capacity;
    private String date;
}
