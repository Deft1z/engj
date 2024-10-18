package com.kge.energy.crm.external.iam.resp;

import lombok.Data;

@Data
public class IamResp<T> {

    public static final Integer SUCCESS_CODE = 0;

    private Integer code;
    private String msg;
    private T data;
    private String timeStamp;
}
