package com.kge.energy.crm.content.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ModuleAddReq  {
    @NotBlank
    private String name;

    @NotBlank
    private String code;

    @NotBlank
    private String type;

    private String remark;
}
