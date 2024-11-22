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
public enum RoleEnums {

    SUPER_ADMIN("super_admin", "超级管理员"),

    TENANT_ADMIN("tenant_admin", "租户管理员"),

    JT_CUSTOMER("jt_customer", "集团客服"),

    SUB_COMPANY_CUSTOMER("sub_company_customer", "二级公司客服"),

    APPLET_USER("applet_user", "小程序用户"),

    JT_LEADER("jt_leader", "集团领导"),

    COMPANY_LEADER("company_leader", "公司领导"),

    OPERATE_ADMIN("operate_admin", "运营管理员"),

    HARMONY_POWER_CONTROL("harmony_power_control", "电鸿控制"),
    ;


    private final String code;

    private final String desc;

    public static RoleEnums getByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> Objects.equals(e.getCode(), code))
                .findFirst()
                .orElse(null);
    }
}
