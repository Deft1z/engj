package com.kge.energy.crm.operation.maintenance.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.external.ecc.req.EccOperationDetailReq;
import com.kge.energy.crm.external.ecc.req.EccReq;
import com.kge.energy.crm.operation.maintenance.req.PatrolRecordReq;
import com.kge.energy.crm.operation.maintenance.service.OperationMaintenanceService;
import com.kge.platform.framework.common.net.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

/**
 * 小程序运维托管接口
 *
 * @author tangchenghui
 */
@RestController
@RequestMapping()
@RequiredArgsConstructor
public class OperationMaintenanceController {

    private final OperationMaintenanceService operationMaintenanceService;

    @ConvertToGoFormats
    @PostMapping("/external/getRecord")
    public CommonResult<Object> getRecordList(@RequestBody EccReq eccReq) throws NoSuchAlgorithmException {
        return CommonResult.suc(operationMaintenanceService.getRecordList(eccReq));
    }

    @PostMapping("/external/getRecordDetail")
    public CommonResult<Object> getRecordList(@RequestBody EccOperationDetailReq req) throws NoSuchAlgorithmException {
        return CommonResult.suc(operationMaintenanceService.getMaintenanceDetail(req));
    }

    @ConvertToGoFormats
    @PostMapping("/om/report/info/load")
    public CommonResult<Object> getPatrolRecordInfo(@RequestBody PatrolRecordReq patrolRecordReq) {
        return CommonResult.suc(operationMaintenanceService.getPatrolRecordInfo(patrolRecordReq));
    }

}
