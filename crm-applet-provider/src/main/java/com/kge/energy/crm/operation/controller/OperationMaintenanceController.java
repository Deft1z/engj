package com.kge.energy.crm.operation.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.external.ecc.req.EccReq;
import com.kge.energy.crm.operation.req.PatrolRecordReq;
import com.kge.energy.crm.operation.service.OperationMaintenanceService;
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
    public CommonResponse<Object> getRecordList(@RequestBody EccReq eccReq) throws NoSuchAlgorithmException {
        return CommonResponse.suc(operationMaintenanceService.getRecordList(eccReq));
    }

    @ConvertToGoFormats
    @PostMapping("/om/report/info/load")
    public CommonResponse<Object> getPatrolRecordInfo(@RequestBody PatrolRecordReq patrolRecordReq){
        return CommonResponse.suc(operationMaintenanceService.getPatrolRecordInfo(patrolRecordReq));
    }

}
