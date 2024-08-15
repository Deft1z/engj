package com.kge.energy.crm.org.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "更新组织name", description = "更新组织对象")
public class UpdateOrgReq {

    @Schema(description = "组织id")
    @NotNull
    private Integer organizationId;

    @Schema(description = "上级组织id")
//    @NotNull
    private Integer parentOrganizationId;

    @Schema(description = "组织名称")
    @NotBlank
    private String name;

    @Schema(description = "排序")
    @NotNull
    private Integer sort;

    @Schema(description = "状态,0正常 1停用")
    @NotNull
    private Integer status;

    @Schema(description = "备注")
    private String remark;

}
