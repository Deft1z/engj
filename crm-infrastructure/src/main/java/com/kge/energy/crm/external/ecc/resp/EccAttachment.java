package com.kge.energy.crm.external.ecc.resp;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EccAttachment {
    /**
     * 合同名字
     */
    private String url;
    /**
     * 合同甲方名称
     */
    private String type;
}
