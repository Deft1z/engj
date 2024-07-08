package com.kge.energy.crm.om.report.req;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ImageModel {
    /**
     * 合同名字
     */
    private String Url;
    /**
     * 合同甲方名称
     */
    private String Type;
}
