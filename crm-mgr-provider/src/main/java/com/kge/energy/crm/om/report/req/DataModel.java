package com.kge.energy.crm.om.report.req;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DataModel {
    /**
     * 页码
     */
    private Integer PageNum;

    /**
     * 一页的数量
     */
    private Integer PageSize;

    /**
     * 页数
     */
    private Integer Size;

    /**
     * 总数
     */
    private Integer Total;

    /**
     * 内容
     */
    private ListModel[]  List;
}
