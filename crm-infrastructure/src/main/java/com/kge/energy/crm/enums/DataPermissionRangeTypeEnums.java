package com.kge.energy.crm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 数据权限范围类型枚举类
 *
 * @author wangjihua
 */
@Getter
@AllArgsConstructor
public enum DataPermissionRangeTypeEnums {

    ALL(0, "所有"),

    TENANT(1, "租户"),

    JITUAN(2, "集团"),

    COMPANY(3, "公司"),

    DEPARTMENT(4, "部门"),

    ONESELF(5, "个人");

    private final Integer code;

    private final String desc;

    public static DataPermissionRangeTypeEnums getByCode(Integer code) {
        return Arrays.stream(values())
                .filter(e -> Objects.equals(e.getCode(), code))
                .findFirst()
                .orElse(null);
    }

    public static DataPermissionRangeTypeEnums getByDesc(String desc) {
        return Arrays.stream(values())
                .filter(e -> Objects.equals(e.getDesc(), desc))
                .findFirst()
                .orElse(null);
    }

    public static Integer getCodeByDesc(String desc) {
        return Arrays.stream(values())
                .filter(e -> Objects.equals(e.getDesc(), desc))
                .findFirst().map(DataPermissionRangeTypeEnums::getCode)
                .orElse(null);
    }

}
