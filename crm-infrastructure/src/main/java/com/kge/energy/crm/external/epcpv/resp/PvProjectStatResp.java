package com.kge.energy.crm.external.epcpv.resp;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PvProjectStatResp {
    private String normalproject;
    private String suspendproject;
    private String normalprojectper;
    private String suspendprojectper;
}
