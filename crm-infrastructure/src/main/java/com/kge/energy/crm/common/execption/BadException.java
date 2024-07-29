package com.kge.energy.crm.common.execption;

import com.kge.energy.crm.common.net.ResponseCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 * 业务异常类
 *
 * @author wangjihua
 */
@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
public class BadException extends RuntimeException {

    private Integer code;

    private String msg;

    private String showType;


    public BadException(ResponseCode responseCode) {
        super(responseCode.getMsg());
        this.code = responseCode.getCode();
        this.msg = responseCode.getMsg();
        this.showType = responseCode.getShowType();
    }

    public BadException(String message) {
        super(message);
        this.code = 500;
        this.msg = message;
        this.showType = "message";
    }

    public BadException(Throwable cause) {
        super(cause);
    }

    public BadException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadException(Integer code, String message, String messagebox) {
        super(message);
        this.code = code;
        this.msg = message;
        this.showType = messagebox;
    }
}
