package com.kge.energy.crm.role.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(name = "角色关联菜单name", description = "角色关联菜单对象")
public class RoleResourceResp {

    @Schema(description = "菜单资源ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private List<Integer> resourceIdList;

}
