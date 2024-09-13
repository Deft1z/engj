package com.kge.energy.crm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FlagEnums {

    NORMAL(1, "正常"),
    DELETED(-1, "删除");

    private final Integer flag;

    private final String desc;
}
