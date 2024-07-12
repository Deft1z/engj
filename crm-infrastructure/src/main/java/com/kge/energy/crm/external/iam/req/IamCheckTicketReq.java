package com.kge.energy.crm.external.iam.req;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IamCheckTicketReq {
    private String appKey;
    private String sign;
}
