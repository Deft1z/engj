package com.kge.energy.crm.common.button.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务工单按钮枚举类
 *
 * @author wangjihua
 */
@Getter
@AllArgsConstructor
public enum ConsultingButtonEnum implements ButtonEnum {

    HANDLE_CONSULTING("handle_consulting", "处理工单", true, null),


    ;

    private final String code;

    private final String name;

    private final Boolean enabled;

    private final String hint;
}
