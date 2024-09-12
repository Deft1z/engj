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

    TOKEN_FAIL(401, "重新登录"),

    ;

    private final Integer code;

    private final String msg;

}
