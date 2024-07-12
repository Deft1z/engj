package com.kge.energy.crm.external.ecc.resp;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EccResp<T> {
    /**
     * 状态码
     */
    private Integer code;

    /**
     * 信息
     */
    private String msg;

    /**
     * 详细数据
     */
    private T data;
}
