package com.kge.energy.crm.om.report.req;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResModel {
    /**
     * 电话
     */
    private int Code;

    /**
     * 页码
     */
    private String Msg;

    /**
     * 详细数据
     */
    private DataModel Data;
}
