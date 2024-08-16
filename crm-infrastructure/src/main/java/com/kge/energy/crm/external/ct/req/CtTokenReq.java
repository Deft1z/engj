package com.kge.energy.crm.external.ct.req;

import lombok.Data;

@Data
public class CtTokenReq {

    private Integer openid;
    private Integer appId;
    private String appSecret;
    private String interfaceAddress;

}
