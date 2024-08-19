package com.kge.energy.crm.app.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ForceBindingReq {

    @NotNull
    private Integer appId;

    @NotNull
    private Integer userId;

    @NotNull
    private Integer anotherId;
}
