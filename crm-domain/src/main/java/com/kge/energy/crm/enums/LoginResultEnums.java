package com.kge.energy.crm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum LoginResultEnums {
    FAIL(0, "失败"),

    SUCCESS(1, "成功");

    private final Integer code;

    private final String desc;

    public static LoginResultEnums getByCode(Integer code) {
        return Arrays.stream(values())
                .filter(e -> Objects.equals(e.getCode(), code))
                .findFirst()
                .orElse(null);
    }
}
