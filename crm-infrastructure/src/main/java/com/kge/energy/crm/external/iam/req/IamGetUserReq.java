package com.kge.energy.crm.external.iam.req;

import lombok.Data;

@Data
public class IamGetUserReq {

    private String token;

    private String appKey;

    /**
     * 校验签名 MD5（appKey+token+secretkey）
     */
    private String sign;
}
