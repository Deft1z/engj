package com.kge.energy.crm.operation.dashboard.controller;

import com.kge.energy.crm.operation.dashboard.req.OperationDashboardReq;
import com.kge.energy.crm.operation.dashboard.service.OperationDashboardService;
import com.kge.energy.crm.repository.entityext.result.StatisticalDataResult;
import com.kge.platform.framework.common.net.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangjihua
 */
@RestController
@RequestMapping("/operation/dashboard")
@RequiredArgsConstructor
public class OperationDashboardController {

    private final OperationDashboardService operationDashboardService;

    @Operation(summary = "客户、工单、合同统计数据")
    @PostMapping("/statisticalData")
    public CommonResult<StatisticalDataResult> statisticalData(@Validated @RequestBody OperationDashboardReq req) {
        return CommonResult.suc(operationDashboardService.statisticalData(req));
    }

}
