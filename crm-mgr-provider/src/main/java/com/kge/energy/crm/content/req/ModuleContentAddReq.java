package com.kge.energy.crm.content.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ModuleContentAddReq {
    private Integer blockId;

    @NotBlank
    private String title;

    private String desc;

    @NotNull
    private Integer imageUrl;

    private Integer pageFile;

    private String remark;
}
