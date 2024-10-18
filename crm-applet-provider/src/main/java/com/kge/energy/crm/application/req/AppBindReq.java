package com.kge.energy.crm.application.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AppBindReq {

    @NotNull
    private Integer appId;

}
