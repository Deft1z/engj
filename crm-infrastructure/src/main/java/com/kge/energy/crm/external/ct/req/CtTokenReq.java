package com.kge.energy.crm.external.ct.req;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CtTokenReq {

    private Integer openid;
    private Integer appId;
    private String appSecret;
    private String interfaceAddress;

}
