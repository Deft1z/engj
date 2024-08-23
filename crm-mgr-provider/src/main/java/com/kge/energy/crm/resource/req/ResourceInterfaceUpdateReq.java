package com.kge.energy.crm.resource.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(name = "更新资源接口请求参数", description = "更新资源接口请求参数")
public class ResourceInterfaceUpdateReq {

    @NotNull
    @Schema(description = "接口ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;

    @Schema(description = "接口名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String interfaceName;

    @Schema(description = "接口地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String interfaceUrl;

    @Schema(description = "请求方式（GET POST PUT DELETE", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Pattern(regexp = "GET|POST|PUT|DELETE")
    private String requestMethod;

    @Schema(description = "状态（0正常 1停用）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer status;
}
