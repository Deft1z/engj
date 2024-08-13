package com.kge.energy.crm.repository.entityext.param;

import com.kge.energy.crm.common.page.PageReq;
import lombok.Data;

@Data
public class OrgQueryParam{
    private String name;
    private Integer parentOrganizationId;
    private Integer tenantId;
    private Integer level;
}
