package com.kge.energy.crm.complain.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ComplainReplyReq {

    @NotNull
    private Integer complainId;

    @NotBlank
    private String feedback;

}
