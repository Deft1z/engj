package com.kge.energy.crm.company.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CompanyEditReq {
    @NotNull
    private Integer organizationId;
    private String name;
    private String fullName;
    private Integer serviceType;
    private String remark;
    private String filePath;
}
