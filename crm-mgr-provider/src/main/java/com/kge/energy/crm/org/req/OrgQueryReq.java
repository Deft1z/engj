package com.kge.energy.crm.org.req;

import com.kge.energy.crm.common.page.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "查询组织列表name", description = "查询组织列表对象")
public class OrgQueryReq{

    @Schema(description = "组织名称")
    private String name;

    @Schema(description = "上级组织id")
    private Integer parentOrganizationId;

    @Schema(description = "租户id")
    private Integer tenantId;
}
