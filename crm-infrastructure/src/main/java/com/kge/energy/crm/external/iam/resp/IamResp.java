package com.kge.energy.crm.external.iam.resp;

import lombok.Data;

@Data
public class IamResp<T> {
    private Integer code;
    private String msg;
    private T data;
    private String timeStamp;
}
