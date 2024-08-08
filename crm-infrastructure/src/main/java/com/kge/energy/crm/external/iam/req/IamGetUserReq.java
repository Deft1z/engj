package com.kge.energy.crm.external.iam.req;

import lombok.Data;

@Data
public class IamGetUserReq {
    private String token;
    private String appKey;
    private String sign;
}
