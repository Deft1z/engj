package com.kge.energy.crm.workOrder.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "撤回退回工单列表对象")
public class FormWithdrawReturnResp {

    @Schema(description = "工单id")
    private Integer formId;

    @Schema(description = "业务名称")
    private String businessName;

    @Schema(description = "工单编码")
    private String code;

    @Schema(description = "提交时间")
    private String timeSubmit;

    @Schema(description = "更新时间")
    private String modifyTime;

    @Schema(description = "服务单位")
    private String companyName;

    @Schema(description = "原因")
    private String actionContent;

    @Schema(description = "节点状态")
    private String status;
}
