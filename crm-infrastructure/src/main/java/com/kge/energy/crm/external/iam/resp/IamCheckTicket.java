package com.kge.energy.crm.external.iam.resp;

import lombok.Data;

@Data
public class IamCheckTicket {
    private IamUserBean userBean;
    private String token;
    private String refreshToken;
    private Integer tokenExpirationDate;
    private Integer refTokenExpirationDate;
}
