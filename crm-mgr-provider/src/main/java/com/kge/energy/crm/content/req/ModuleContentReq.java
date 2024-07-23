package com.kge.energy.crm.content.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ModuleContentReq extends ModuleReq {
    @NotNull
    private Integer blockId;
}
