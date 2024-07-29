package com.kge.energy.crm.repository.entityext.result;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class OrgResult {
    private Integer organizationId;
    private String fullName;
    private Integer serviceType;
    private String type;
    private List<String> typeList;
    private String filepath;
}
