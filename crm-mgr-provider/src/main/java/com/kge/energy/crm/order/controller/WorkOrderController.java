package com.kge.energy.crm.order.controller;

import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.order.req.GetFlowByFormIdReq;
import com.kge.energy.crm.order.req.WorkOrdeUpdateReq;
import com.kge.energy.crm.order.req.WorkOrderListReq;
import com.kge.energy.crm.order.resp.FlowResp;
import com.kge.energy.crm.order.resp.FormResp;
import com.kge.energy.crm.order.service.WorkOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 工单管理
 *
 * @author wangjihua
 */
@RestController
@RequestMapping("/workMgrBack/consultingBack")
@RequiredArgsConstructor
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    /**
     * 工单列表
     */
    @PostMapping("/order")
    public CommonResponse<PageResp<FormResp>> list(@Validated @RequestBody WorkOrderListReq req) {
        return CommonResponse.suc(workOrderService.list(req));
    }


    /**
     * 通过工单ID获取工单流程
     */
    @PostMapping("/getFlowByFormId")
    public CommonResponse<List<FlowResp>> getFlowByFormId(@Validated @RequestBody GetFlowByFormIdReq req) {
        return CommonResponse.suc(workOrderService.getFlowByFormId(req));
    }

    /**
     * 分派工单 终止工单 处理工单
     */
    @PostMapping("/workOrderUpdate")
    public CommonResponse<Integer> workOrderUpdate(WorkOrdeUpdateReq req) {
        return CommonResponse.suc(workOrderService.workOrderUpdate(req));

    }
}
