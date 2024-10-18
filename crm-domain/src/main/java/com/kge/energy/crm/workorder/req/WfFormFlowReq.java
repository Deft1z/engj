package com.kge.energy.crm.workorder.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "工单查询参数")
public class WfFormFlowReq {

    @Schema(description = "工单id")
    private Integer formId;

}
