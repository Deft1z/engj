package com.kge.energy.crm.content.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ModuleDeleteReq {
    @NotNull
    private List<Integer> ids;
}
