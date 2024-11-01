package com.kge.energy.crm.complain.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ComplainDetailReq {

    @NotNull
    private Integer complainId;

}
