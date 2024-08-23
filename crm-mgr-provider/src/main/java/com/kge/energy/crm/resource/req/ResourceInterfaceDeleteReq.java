package com.kge.energy.crm.resource.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(name = "删除资源接口请求参数", description = "删除资源接口请求参数")
public class ResourceInterfaceDeleteReq {

    @NotNull
    @Schema(description = "接口ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;
}
