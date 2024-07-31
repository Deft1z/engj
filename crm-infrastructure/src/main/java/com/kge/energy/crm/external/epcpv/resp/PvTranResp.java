package com.kge.energy.crm.external.epcpv.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class PvTranResp {
    private String name;
    private String count;
    private String per;
    List<PvTranResp> detail;
}
