package com.kge.energy.crm.user.req;

import com.kge.energy.crm.common.page.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "角色获取用户列表请求参数", description = "角色获取用户列表请求参数")
public class UserListByRoleReq extends PageReq {

    @Schema(description = "租户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer tenantId;

    @Schema(description = "角色Id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer roleId;

    @Schema(description = "用户名称")
    private String name;

    @Schema(description = "真实姓名")
    private String realname;

    @Schema(description = "手机号码")
    private String mobile;

}
