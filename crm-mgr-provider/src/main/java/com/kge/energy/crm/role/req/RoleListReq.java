package com.kge.energy.crm.role.req;

import com.kge.energy.crm.common.page.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(name = "角色列表请求参数", description = "角色列表请求参数")
public class RoleListReq extends PageReq {

    @Schema(description = "租户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer tenantId;

}
