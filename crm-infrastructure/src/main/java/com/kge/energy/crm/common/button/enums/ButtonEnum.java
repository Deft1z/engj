package com.kge.energy.crm.common.button.enums;

/**
 * 建议各自业务用到 code 定义不一致，后续可兼容数据库控制
 *
 * @author wangjihua
 * @since 1.0.0
 */
public interface ButtonEnum {

    String getCode();

    String getName();

    Boolean getEnabled();

    String getHint();

}
