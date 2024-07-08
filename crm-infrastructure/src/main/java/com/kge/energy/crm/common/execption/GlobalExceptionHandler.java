package com.kge.energy.crm.common.execption;

import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.common.net.ResponseCode;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理
 *
 * @author wangjihua
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BadException.class)
    public <T> CommonResponse<T> handleBadException(BadException e) {
        return errorResult(e.getResponseCode(), e);
    }

    @ExceptionHandler(value = {
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            Exception.class
    })
    public <T> CommonResponse<T> handleConstraintViolationException(Exception e) {
        return errorResult(ResponseCode.UNKNOWN, e);
    }

    public <T> CommonResponse<T> errorResult(ResponseCode responseCode, Exception e) {
        return errorResult(responseCode.getCode(), responseCode.getMsg(), responseCode.getShowType(), e);
    }

    public <T> CommonResponse<T> errorResult(Integer code, String msg, String showType, Exception e) {

        log.error("==> Error code [{}], msg [{}], showType [{}]", code, msg, showType, e);

        return new CommonResponse()
                .setErrorCode(code)
                .setErrorMsg(msg)
                .setShowType(showType)
                .setData(null);
    }
}
