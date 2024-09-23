package com.kge.energy.crm.workOrder.resp;

import com.kge.energy.crm.common.button.resp.BaseButton;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@Schema(description = "工单流程详情")
public class WfFormFlowResp {

    @Schema(description = "工单流程节点")
    private List<WfFormFlowListResp> wfFormFlowList;

    @Schema(description = "工单操作按钮")
    private List<BaseButton> buttonList;

}
