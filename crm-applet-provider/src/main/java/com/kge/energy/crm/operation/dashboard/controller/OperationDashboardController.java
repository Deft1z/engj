package com.kge.energy.crm.operation.dashboard.controller;

import com.kge.energy.crm.operation.dashboard.req.OperationDashboardReq;
import com.kge.energy.crm.operation.dashboard.service.OperationDashboardService;
import com.kge.energy.crm.operation.data.resp.OperationDataOrgResp;
import com.kge.energy.crm.repository.entityext.result.StatisticalDataResult;
import com.kge.platform.framework.common.net.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangjihua
 */
@RestController
@RequestMapping("/operation/record")
@RequiredArgsConstructor
@Tag(name = "运营报告API")
public class OperationDashboardController {

    private final OperationDashboardService operationDashboardService;

    @Operation(summary = "公司筛选列表")
    @GetMapping("/orgList")
    public CommonResult<List<OperationDataOrgResp>> orgList() {
        return CommonResult.suc(operationDashboardService.orgList());
    }

    @Operation(summary = "客户、工单、合同统计数据")
    @PostMapping("/statisticalData")
    public CommonResult<StatisticalDataResult> statisticalData(@Validated @RequestBody OperationDashboardReq req) {
        return CommonResult.suc(operationDashboardService.statisticalData(req));
    }

}
