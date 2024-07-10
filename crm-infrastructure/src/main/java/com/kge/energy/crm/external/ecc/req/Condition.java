package com.kge.energy.crm.external.ecc.req;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class Condition {
    /**
     * 操作员
     */
    private String firstPartyContactsPhone;

    /**
     * 类型
     */
    private String[] riskRates;
}
