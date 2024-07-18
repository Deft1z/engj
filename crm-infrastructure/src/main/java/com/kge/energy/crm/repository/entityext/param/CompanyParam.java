package com.kge.energy.crm.repository.entityext.param;

import com.kge.energy.crm.common.page.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CompanyParam extends PageReq {
    private Integer organizationId;
    private String name;
    private String fullName;
    private Integer serviceType;
    private String remark;
    private String filePath;
}
