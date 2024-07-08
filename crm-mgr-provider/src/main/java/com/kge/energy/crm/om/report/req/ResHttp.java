package com.kge.energy.crm.om.report.req;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResHttp {
    /**
     * 页码
     */
    private Integer ErrorCode;

    /**
     * 错误提示
     */
    private String ErrorMsg;

    /**
     *  展示提示
     */
    private String ShowType;

    /**
     * 筛选条件
     */
    private ResModel Data;
}
