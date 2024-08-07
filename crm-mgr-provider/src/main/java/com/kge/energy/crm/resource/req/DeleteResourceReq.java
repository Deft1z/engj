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
@Schema(name = "删除菜单资源name", description = "删除菜单资源对象")
public class DeleteResourceReq {

    @Schema(description = "资源ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer resourceId;


}
