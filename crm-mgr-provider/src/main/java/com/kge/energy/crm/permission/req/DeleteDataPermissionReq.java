package com.kge.energy.crm.permission.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(name = "删除数据权限配置", description = "删除数据权限配置对象")
public class DeleteDataPermissionReq {

    @Schema(description = "配置ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer id;
}
