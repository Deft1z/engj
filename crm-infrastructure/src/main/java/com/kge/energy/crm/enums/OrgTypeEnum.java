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
public enum OrgTypeEnum {

    GROUP(0, "集团"),

    COMPANY(1, "公司"),

    DEPARTMENT(2, "部门"),

    PROJECT_TEARM(3, "项目"),

    ;

    private final Integer code;

    private final String desc;

    public static OrgTypeEnum getByCode(Integer code) {
        return Arrays.stream(values())
                .filter(e -> Objects.equals(e.getCode(), code))
                .findFirst()
                .orElse(null);
    }
}
