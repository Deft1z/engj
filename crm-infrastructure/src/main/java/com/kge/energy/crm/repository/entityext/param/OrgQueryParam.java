package com.kge.energy.crm.repository.entityext.param;

import lombok.Data;

import java.util.List;

@Data
public class OrgQueryParam {
    private String name;
    private Integer parentOrganizationId;
    private Integer tenantId;
    private Integer level;
    private List<Integer> orgIds;
}
