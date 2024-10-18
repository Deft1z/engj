package com.kge.energy.crm.function.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(name = "新增配置", description = "新增配置对象")
public class AddBizFunctionReq {

    @Schema(description = "租户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer tenantId;

    @Schema(description = "业务模块名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String moduleName;

    @Schema(description = "业务模块编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String moduleCode;

    @Schema(description = "业务功能名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String functionName;

    @Schema(description = "业务功能编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String functionCode;

}
