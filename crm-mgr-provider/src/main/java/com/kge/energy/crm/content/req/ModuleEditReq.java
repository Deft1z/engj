package com.kge.energy.crm.content.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)

public class ModuleEditReq extends ModuleAddReq {
    @NotNull
    private Integer blockId;

    // TODO 后续要修改成其他字段来表示启用/禁用状态
    private Integer flag;
}
