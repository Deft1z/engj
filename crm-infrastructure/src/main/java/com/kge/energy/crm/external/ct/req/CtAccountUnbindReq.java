package com.kge.energy.crm.external.ct.req;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CtAccountUnbindReq {
    private Integer openId;
    private Integer appId;
    private String appSecret;
    private String interfaceAddress;
}
