package com.kge.energy.crm.om.report.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Condition {
    /**
     * 操作员
     */
    private String FirstPartyContactsPhone;

    /**
     * 类型
     */
    private String[] RiskRates;

}
