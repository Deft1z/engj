package com.kge.energy.crm.user.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(name = "移除用户角色请求参数", description = "移除用户角色请求参数")
public class RemoveUserRoleReq {

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer userId;

    @Schema(description = "角色ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Set<Integer> roleIds;

}
