package com.kge.energy.crm.sso.resp;

import lombok.Data;

@Data
public class SSOResp {
    private String token;
    private int    userId;
}
