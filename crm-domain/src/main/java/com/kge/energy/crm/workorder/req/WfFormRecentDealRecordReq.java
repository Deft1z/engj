package com.kge.energy.crm.workorder.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.wildfly.common.annotation.NotNull;

@Data
@Accessors(chain = true)
@Schema(description = "获取业务工单最近处理记录内容参数")
public class WfFormRecentDealRecordReq {


    @NotNull
    @Schema(description = "按钮操作类型")
    private String operateType;
    
}