package com.kge.energy.crm.sso.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SSOReq {

    @NotBlank
    private String ticket;
}
