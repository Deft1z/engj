package com.kge.energy.crm.external.epcpv.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class EpcpvInfoReq {
    private String queryDateStart;
    private String queryDateEnd;
}
