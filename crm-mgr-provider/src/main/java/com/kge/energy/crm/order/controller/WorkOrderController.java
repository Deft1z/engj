package com.kge.energy.crm.order.controller;

import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.order.req.WorkOrderListReq;
import com.kge.energy.crm.order.resp.FormModelResp;
import com.kge.energy.crm.order.service.WorkOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public CommonResponse<PageResp<FormModelResp>> list(@Validated @RequestBody WorkOrderListReq req) {
        return CommonResponse.suc(workOrderService.list(req));
    }
}
