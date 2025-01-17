package com.kge.energy.crm.operation.dashboard.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.kge.energy.crm.operation.dashboard.req.OperationDashboardReq;
import com.kge.energy.crm.operation.dashboard.service.OperationDashboardService;
import com.kge.energy.crm.operation.data.resp.OperationDataOrgResp;
import com.kge.energy.crm.repository.entityext.result.DashboardStatResult;
import com.kge.energy.crm.repository.entityext.result.StatisticalDataResult;
import com.kge.platform.framework.common.net.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangjihua
 */
@RestController
@RequestMapping("/operation/dashboard")
@RequiredArgsConstructor
@Tag(name = "运营看板API")
public class OperationDashboardController {

    private final OperationDashboardService operationDashboardService;

    @ApiOperationSupport(order = 1)
    @Operation(summary = "公司筛选列表")
    @GetMapping("/orgList")
    public CommonResult<List<OperationDataOrgResp>> orgList() {
        return CommonResult.suc(operationDashboardService.orgList());
    }

    @ApiOperationSupport(order = 2)
    @Operation(summary = "客户、工单、合同统计数据")
    @PostMapping("/statisticalData")
    public CommonResult<StatisticalDataResult> statisticalData(@Validated @RequestBody OperationDashboardReq req) {
        return CommonResult.suc(operationDashboardService.statisticalData(req));
    }

    @ApiOperationSupport(order = 3)
    @Operation(summary = "新用户增长曲线数据")
    @PostMapping("/newUserGrowthData")
    public CommonResult<DashboardStatResult> newUserGrowthData(@Validated @RequestBody OperationDashboardReq req) {
        return CommonResult.suc(operationDashboardService.newUserGrowthData(req));
    }

    @ApiOperationSupport(order = 4)
    @Operation(summary = "工单合同数量变化数据")
    @PostMapping("/orderContractQtyData")
    public CommonResult<DashboardStatResult> orderContractData(@Validated @RequestBody OperationDashboardReq req) {
        return CommonResult.suc(operationDashboardService.orderContractQtyData(req));
    }

    @ApiOperationSupport(order = 5)
    @Operation(summary = "合同金额变化数据")
    @PostMapping("/contractAmountData")
    public CommonResult<DashboardStatResult> contractAmountData(@Validated @RequestBody OperationDashboardReq req) {
        return CommonResult.suc(operationDashboardService.orderContractAmountData(req));
    }

    @ApiOperationSupport(order = 6)
    @Operation(summary = "投诉类型占比数据")
    @PostMapping("/complainPctData")
    public CommonResult<DashboardStatResult> complainPctData(@Validated @RequestBody OperationDashboardReq req) {
        return CommonResult.suc(operationDashboardService.complainPctData(req));
    }

    @ApiOperationSupport(order = 7)
    @Operation(summary = "投诉类型数量变化数据")
    @PostMapping("/complainQtyData")
    public CommonResult<DashboardStatResult> complainTypeData(@Validated @RequestBody OperationDashboardReq req) {
        return CommonResult.suc(operationDashboardService.complainQtyData(req));
    }

    @Operation(summary = "运营看板数据导出")
    @PostMapping("/export")
    public void exportStatistic(HttpServletResponse response, @Validated @RequestBody OperationDashboardReq req) {
        operationDashboardService.exportStatistic(response, req);
    }

}
