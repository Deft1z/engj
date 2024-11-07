package com.kge.energy.crm.workorder.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Schema(description = "工单服务类型")
@Accessors(chain = true)
public class WfFormRecentDealRecordResp {

    @Schema(description = "返回最近处理记录内容")
    private List<String> dealRecord;

}