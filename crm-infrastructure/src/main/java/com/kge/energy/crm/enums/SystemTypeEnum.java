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
public enum SystemTypeEnum {

    MGR("mgr", "管理后台"),

    APPLET("applet", "微信小程序");


    private final String code;

    private final String desc;

    public static SystemTypeEnum getByCode(Integer code) {
        return Arrays.stream(values())
                .filter(e -> Objects.equals(e.getCode(), code))
                .findFirst()
                .orElse(null);
    }
}
