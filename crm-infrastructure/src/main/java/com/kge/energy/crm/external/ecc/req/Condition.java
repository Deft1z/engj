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

    /**
     * 施工单位
     */
    private String sysCompanyCode;

    /**
     * 合同/项目名称
     */
    private String keyword;

    /**
     * 合格/不合格
     */
    private String remarkC;

    /**
     * 开始时间
     */
    private String startDate;

    /**
     * 结束时间
     */
    private String endDate;
}
