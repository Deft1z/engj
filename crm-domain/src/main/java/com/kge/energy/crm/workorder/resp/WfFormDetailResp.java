package com.kge.energy.crm.workorder.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "工单详情")
public class WfFormDetailResp {

    @Schema(description = "工单id")
    private Integer formId;

    @Schema(description = "业务咨询")
    private String businessName;

    @Schema(description = "提交时间")
    private String timeSubmit;

    @Schema(description = "工单编号")
    private String code;

    @Schema(description = "工单状态")
    private String subStatus;

    @Schema(description = "受理单位")
    private String orgName;

    @Schema(description = "受理人员")
    private String realname;

    @Schema(description = "备注")
    private String remark;

}
