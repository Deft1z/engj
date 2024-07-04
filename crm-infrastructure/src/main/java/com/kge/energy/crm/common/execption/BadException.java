package com.kge.energy.crm.common.execption;

import com.kge.energy.crm.common.net.ResponseCode;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 业务异常类
 * @author wangjihua
 */
@Getter
@Setter
public class BadException extends RuntimeException{

    private ResponseCode responseCode;

    public BadException(ResponseCode responseCode) {
        super(responseCode.getMsg());
        this.responseCode = responseCode;
    }

    public BadException(String message) {
        super(message);
    }

    public BadException(Throwable cause) {
        super(cause);
    }

    public BadException(String message, Throwable cause) {
        super(message, cause);
    }
}
