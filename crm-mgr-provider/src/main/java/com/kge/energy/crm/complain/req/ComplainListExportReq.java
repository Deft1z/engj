package com.kge.energy.crm.complain.req;

import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode
@Data
public class ComplainListExportReq {

    private Integer tenantId;

    private ComplainListSearchMap searchMap;
}
