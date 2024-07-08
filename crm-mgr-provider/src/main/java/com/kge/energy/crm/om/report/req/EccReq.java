package com.kge.energy.crm.om.report.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class EccReq {
    /**
     * 页码
     */
    private Integer pageNo;

    /**
     * 一页页记录大小
     */
    private Integer pageSize;

    /**
     * 筛选条件
     */
    private Condition condition;
}
