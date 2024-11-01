package com.kge.energy.crm.complain.req;

import com.kge.energy.crm.easyexcel.ExportReq;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode
@Data
public class ComplainListExportReq extends ExportReq {

    private Integer tenantId;

    private ComplainListSearchMap searchMap;
}
