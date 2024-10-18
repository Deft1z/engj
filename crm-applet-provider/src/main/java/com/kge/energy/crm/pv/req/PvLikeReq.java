package com.kge.energy.crm.pv.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PvLikeReq {
    @NotNull
    private Integer id;

    private Integer status;
}
