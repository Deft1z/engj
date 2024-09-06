package com.kge.energy.crm.application.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AppTokenReq {

    @NotNull
    private Integer appId;

}
