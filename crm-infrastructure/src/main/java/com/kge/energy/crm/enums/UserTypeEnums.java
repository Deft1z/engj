package com.kge.energy.crm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wangjihua
 */
@Getter
@AllArgsConstructor
public enum UserTypeEnums {

    SYSTEM_USERS("系统用户"),

    SOCIAL_CUSTOMERS("社会客户"),

    LEADER("领导");

    private final String desc;

}