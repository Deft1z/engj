package com.kge.energy.crm.pv.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PvCommentReq {
    private Integer id;

    @NotBlank
    private String content;
}
