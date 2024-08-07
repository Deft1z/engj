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
@Schema(name = "角色列表name", description = "角色列表对象")
public class RoleListResp {

    private List<Role> roles;

    @Data
    @Accessors(chain = true)
    public static class Role {

        @Schema(description = "角色ID")
        private Integer roleId;

        @Schema(description = "角色名称")
        private String name;

        @Schema(description = "角色编码")
        private String code;

        @Schema(description = "角色状态（0正常 1停用）")
        private Integer status;

        @Schema(description = "备注")
        private String remark;

    }
}
