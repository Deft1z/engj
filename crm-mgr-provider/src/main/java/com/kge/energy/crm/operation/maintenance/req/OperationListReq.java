package com.kge.energy.crm.operation.maintenance.req;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OperationListReq {
    /**
     * 电话
     */
    private String phone;

    /**
     * 页码
     */
    private Integer pageNo;

    /**
     * 分页大小
     */
    private Integer pageSize;
}
