package com.kge.energy.crm.workorder.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.wildfly.common.annotation.NotNull;

@Data
@Accessors(chain = true)
@Schema(description = "获取业务工单最近处理记录内容参数")
public class WfFormRecentDealRecordReq {

    /**
     * 点击对应按钮时传参指定操作类型
     * 集团客服：
     * assignWorkOrder 分派工单
     * withdrawWorkOrder 撤回工单
     * terminateWorkOrder 终止工单
     * 二级公司客服;
     * dealWorkOrder 处理工单
     * returnWorker 退回工单
     * completeWorkOrder 完结工单
     */
    @NotNull
    @Schema(description = "操作类型")
    private String operateType;
    
}