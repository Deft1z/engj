package com.kge.energy.crm.repository.entityext.param;

import com.kge.energy.crm.common.page.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ComplainListParam extends PageReq {
    private String name;
    private Integer status;
    private Integer tenantId;
    private Integer createUserId;
}
