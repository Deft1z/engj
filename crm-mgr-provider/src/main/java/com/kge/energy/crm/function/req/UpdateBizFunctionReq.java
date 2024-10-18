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
@Schema(name = "更新配置", description = "更新配置对象")
public class UpdateBizFunctionReq {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer id;

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
