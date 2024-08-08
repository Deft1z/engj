package com.kge.energy.crm.repository.entityext.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ModuleContentParam extends ModuleParam {
    private Integer blockId;
}
