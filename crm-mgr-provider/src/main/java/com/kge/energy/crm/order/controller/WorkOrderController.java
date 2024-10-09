package com.kge.energy.crm.order.controller;

import com.kge.energy.crm.comment.req.WfFormCommentReq;
import com.kge.energy.crm.comment.service.CmsCommentService;
import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.order.req.WorkOrderExportReq;
import com.kge.energy.crm.order.req.WxUserWorkOrderReq;
import com.kge.energy.crm.order.resp.FormResp;
import com.kge.energy.crm.order.service.WorkOrderService;
import com.kge.energy.crm.workOrder.req.WfFormFlowReq;
import com.kge.energy.crm.workOrder.req.WfFormPageReq;
import com.kge.energy.crm.workOrder.req.WorkOrderUpdateReq;
import com.kge.energy.crm.workOrder.resp.WfFormFlowResp;
import com.kge.energy.crm.workOrder.resp.WfFormPageResp;
import com.kge.energy.crm.workOrder.service.WorkOrderDomainService;
import com.kge.platform.framework.common.net.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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

    private final CmsCommentService cmsCommentService;

    private final WorkOrderDomainService workOrderDomainService;

    /**
     * 工单列表
     */
    @ConvertToGoFormats
    @PostMapping("/order")
    public CommonResult<PageResp<WfFormPageResp>> getByPage(@Validated @RequestBody WfFormPageReq req) {
        return CommonResult.suc(workOrderDomainService.getByPage(req));
    }

    /*
     * 工单列表导出
     * */
    @PostMapping("/order/export")
    public CommonResult<Boolean> workOrderExport(HttpServletResponse response, @Validated @RequestBody WorkOrderExportReq req) throws IOException {
        return CommonResult.suc(workOrderService.exportWorkOrder(response, req));
    }


    /**
     * 通过工单ID获取工单流程
     */
    @ConvertToGoFormats
    @PostMapping("/getFlowByFormId")
    public CommonResult<WfFormFlowResp> getFlowByFormId(@Validated @RequestBody WfFormFlowReq req) {
        return CommonResult.suc(workOrderDomainService.getFlowByFormId(req));
    }


    /**
     * 分派工单 终止工单 处理工单
     */
    @ConvertToGoFormats
    @PostMapping("/order/update")
    public CommonResult<Object> workOrderUpdate(@Validated @RequestBody WorkOrderUpdateReq req) {
        return CommonResult.suc(workOrderDomainService.updateWorkOrder(req));

    }

    /**
     * 小程序客户 -> 多条件查询工单
     */
    @ConvertToGoFormats
    @PostMapping("/workOrderByUserIdLoad")
    public CommonResult<PageResp<FormResp>> workOrderByUserIdLoad(@Validated @RequestBody WxUserWorkOrderReq req) {
        return CommonResult.suc(workOrderService.getWxUserOrder(req));

    }

    @Operation(summary = "工单节点评论")
    @PostMapping(value = "/addComment")
    public CommonResult<Boolean> addComment(@RequestBody @Valid WfFormCommentReq req) {
        return CommonResult.suc(cmsCommentService.addWfFormFlowComment(req));
    }
}
