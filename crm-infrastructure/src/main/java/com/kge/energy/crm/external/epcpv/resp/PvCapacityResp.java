package com.kge.energy.crm.external.epcpv.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PvCapacityResp {
    private String total;
    private String own;
    private String ownper;
    private String other;
    private String otherper;
    private List<PvCapacityItemResp> items;
}
