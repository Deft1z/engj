package com.kge.energy.crm.operation.maintenance.req;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class PatrolRecordReq {
    private String patrolRecordCode;
}
