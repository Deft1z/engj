package com.kge.energy.crm.external.wechat.applet.req.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ContractFinishValueReq {

    private String value;

}
