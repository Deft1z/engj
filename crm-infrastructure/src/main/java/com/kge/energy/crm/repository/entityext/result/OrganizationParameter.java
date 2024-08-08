package com.kge.energy.crm.repository.entityext.result;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class OrganizationParameter {
    private String type;

    private String filepath;

    private String fullName;

    private Integer serviceType;
}
