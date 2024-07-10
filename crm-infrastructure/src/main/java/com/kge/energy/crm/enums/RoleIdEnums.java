package com.kge.energy.crm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author wangjihua
 */
@Getter
@AllArgsConstructor
public enum RoleIdEnums {

    SYSTEM_ADMINISTRATOR(1, "System Administrator"),

    GROUP_CS(2, "Group CS"),

    SUB_CS(3, "Sub CS"),

    CUSTOMER(5, "Customer");;


    private final Integer code;

    private final String desc;

    public static RoleIdEnums getByCode(Integer code) {
        return Arrays.stream(values())
                .filter(e -> Objects.equals(e.getCode(), code))
                .findFirst()
                .orElse(null);
    }
}