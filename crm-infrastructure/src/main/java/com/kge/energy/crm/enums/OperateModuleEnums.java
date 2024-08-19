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
public enum OperateModuleEnums {

    TENANT(0, "租户"),

    ORGANIZATION(1, "组织"),

    USER(2, "用户"),

    ROLE(3, "角色"),

    RESOURCE(4, "菜单"),

    RESOURCE_INTERFACE(5, "资源接口"),

    BAPP(6, "用户家园应用");


    private final Integer code;

    private final String desc;

    public static OperateModuleEnums getByCode(Integer code) {
        return Arrays.stream(values())
                .filter(e -> Objects.equals(e.getCode(), code))
                .findFirst()
                .orElse(null);
    }
}
