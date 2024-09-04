package com.kge.energy.crm.external.ecc.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EccOrgResp {
    private String name;

    @JsonProperty("sysCompanyCode")
    private String eccOrgCode;
}
