package com.kge.energy.crm.common.net;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author wangjihua
 */
@Getter
@AllArgsConstructor
public enum ResponseCode implements Serializable {

    SUC(0, "", "none"),

    PARAM_NOT_VALID(1, "参数验证失败", "message"),

    SHOULD_LOGIN(2, "登录失败", "message"),

    UNKNOWN(7, "未知错误", "messagebox"),

    TOKEN_FAIL(16, "重新登录", "message"),

    AUTHORITY_FAIL(24, "权限不足", "message"),


    ;

    private final Integer code;

    private final String msg;

    private final String showType;
}
