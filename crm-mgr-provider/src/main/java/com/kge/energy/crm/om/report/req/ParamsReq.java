package com.kge.energy.crm.om.report.req;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ParamsReq {
    /**
     * 电话
     */
    private String Phone;

    /**
     * 页码
     */
    private Integer PageNo;

    /**
     * 一页的数量
     */
    private Integer PageSize;
}
