package com.kge.energy.crm.external.iam.req;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class IamCheckTicketReq {

    private String ticket;

    private String appKey;

    // 校验签名 MD5（appKey+ticket+secretkey）
    private String sign;
}
