package com.kge.energy.crm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum LoginPlatformEnums {

    WECHAT_APPLET(1, "微信小程序"),

    PC(2, "pc");

    private final Integer code;

    private final String desc;

    public static LoginPlatformEnums getByCode(Integer code) {
        return Arrays.stream(values())
                .filter(e -> Objects.equals(e.getCode(), code))
                .findFirst()
                .orElse(null);
    }

}
