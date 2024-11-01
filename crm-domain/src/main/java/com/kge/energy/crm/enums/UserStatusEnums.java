package com.kge.energy.crm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 用户状态枚举类
 */
@Getter
@AllArgsConstructor
public enum UserStatusEnums {

    NORMAL(0, "正常"),

    FORBIDDEN(1, "禁用");

    private final Integer code;

    private final String desc;

    public static UserStatusEnums getByCode(Integer code) {
        return Arrays.stream(values())
                .filter(e -> Objects.equals(e.getCode(), code))
                .findFirst()
                .orElse(null);
    }
}
