package com.kge.energy.crm.order.req.contract;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateContractReq {

    @NotNull
    private Integer    formId;

    @NotBlank
    private String name;

    private String company;

    @NotBlank
    private String code;

    private String amount;
    private String projectCode;
    private String projectStartTime;
    private String projectEndTime;
    private String signingTime;
    private String serviceStartTime;
    private String serviceEndTime;
    private Integer    serviceUnit;
    private String content;
    private Integer    owner;
    private Integer    pm;
    private String remark;
}
