package com.kge.energy.crm.external.ecc.req;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class EccReq {
    /**
     * 页码
     */
    private int pageNo;

    /**
     * 分页大小
     */
    private int pageSize;

    /**
     * 筛选条件
     */
    private Condition condition;
}
