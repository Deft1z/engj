package com.kge.energy.crm.external.ecc.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EccOperationDetailReq {

    @NotBlank
    private String planId;

}
