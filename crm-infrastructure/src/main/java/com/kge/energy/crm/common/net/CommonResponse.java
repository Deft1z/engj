package com.kge.energy.crm.common.net;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Objects;

/**
 * 通讯载体, 使用类方法创建实体
 *
 * @author wangjihua
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CommonResponse<T> implements Serializable {

    /**
     * 响应业务码
     */
    private Integer errorCode;

    /**
     * 响应信息
     */
    private String errorMsg;

    /**
     * 错误信息
     */
    private String showType;

    /**
     * 响应数据
     */
    private T data;

    public CommonResponse(Integer errorCode, String errorMsg, String showType, T data) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.showType = showType;
        this.data = data;
    }

    public CommonResponse(ResponseCode responseCode, T data) {
        Objects.requireNonNull(responseCode, "响应码不能为空");
        this.errorCode = responseCode.getCode();
        this.errorMsg = responseCode.getMsg();
        this.showType = responseCode.getShowType();
        this.data = data;
    }

    public static <T> CommonResponse<T> suc(T data) {
        return new CommonResponse<T>(ResponseCode.SUC, data);
    }

    public static <T> CommonResponse<T> suc(ResponseCode responseCode, T data) {
        return new CommonResponse<T>(responseCode, data);
    }

    public static <T> CommonResponse<T> bad(ResponseCode responseCode, T data) {
        return new CommonResponse<T>(responseCode, data);
    }

    public static <T> CommonResponse<T> bad(Integer errorCode, T data) {
        return new CommonResponse<T>(errorCode, null, null, data);
    }

    public static <T> CommonResponse<T> compatible(T data) {
        return new CommonResponse<T>()
                .setData(data);
    }


}
