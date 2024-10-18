package com.kge.energy.crm.common.button.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class BaseButton {

    /**
     * 名称
     */
    private String name;

    /**
     * code
     */
    private String code;

    /**
     * 是否启用
     */
    private Boolean enabled = true;

    /**
     * 点击的提示
     */
    private String hint;

}
