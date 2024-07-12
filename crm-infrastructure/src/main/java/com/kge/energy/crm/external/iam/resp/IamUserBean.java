package com.kge.energy.crm.external.iam.resp;

import lombok.Data;

import java.util.List;

@Data
public class IamUserBean {
    private String userId;
    private String userName;
    private String loginName;
    private String phone;
    private String email;
    private List<IamUserAttrs> userAttrs;
}
