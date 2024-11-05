package com.kge.energy.crm.operation.data.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class OperationDataOrgResp {

    @Schema(description = "组织ID")
    private Integer orgId;

    @Schema(description = "组织名称")
    private String orgName;

}
