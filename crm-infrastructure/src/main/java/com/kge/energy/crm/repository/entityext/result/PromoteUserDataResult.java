package com.kge.energy.crm.repository.entityext.result;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PromoteUserDataResult {
    private String realname;
    private String mobile;
    private String orgName;
    private String promoteUserOpenId;
    private String promoteUserName;
    private String promoteUserPhone;
    private String promoteUserCreateTime;
}
