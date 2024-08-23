package com.kge.energy.crm.complain.req;

import com.kge.energy.crm.common.page.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ComplainListReq extends PageReq {
    private ComplainListSearchMap searchMap;
}
