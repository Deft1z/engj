package com.kge.energy.crm.application.resp;

import lombok.Data;

@Data
public class AppTokenResp {
    private String openId;
    private String rolePkId;
    private String token;
    private String userName;
}
