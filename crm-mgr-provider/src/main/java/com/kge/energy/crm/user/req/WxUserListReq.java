package com.kge.energy.crm.user.req;

import com.kge.energy.crm.common.page.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WxUserListReq extends PageReq {

    @Schema(description = "租户ID")
    private Integer tenantId;

    @Schema(description = "搜索参数")
    private String name;

}
