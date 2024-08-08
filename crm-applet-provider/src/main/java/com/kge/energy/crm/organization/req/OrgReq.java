package com.kge.energy.crm.organization.req;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrgReq {
    private String area;
    private Integer serviceType;
}
