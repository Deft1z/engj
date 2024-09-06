package com.kge.energy.crm.role.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(name = "获取用户角色响应参数", description = "获取用户角色响应参数")
public class UserRoleResp {


    private List<Role> roles;

    @Data
    @Accessors(chain = true)
    public static class Role {

        @Schema(description = "角色ID")
        private Integer roleId;

        @Schema(description = "角色名称")
        private String name;

    }

}
