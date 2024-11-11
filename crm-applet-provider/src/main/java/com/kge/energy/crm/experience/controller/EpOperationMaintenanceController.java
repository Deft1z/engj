package com.kge.energy.crm.experience.controller;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.external.ecc.req.EccOperationDetailReq;
import com.kge.energy.crm.external.ecc.req.EccReq;
import com.kge.energy.crm.external.ecc.resp.EccMaintenance;
import com.kge.energy.crm.operation.maintenance.req.PatrolRecordReq;
import com.kge.energy.crm.operation.maintenance.service.OperationMaintenanceService;
import com.kge.platform.framework.common.net.CommonResult;
import com.kge.platform.framework.web.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 小程序运维托管接口
 *
 * @author tangchenghui
 */
@RestController
@RequestMapping("/experience")
@RequiredArgsConstructor
public class EpOperationMaintenanceController {

    private final OperationMaintenanceService operationMaintenanceService;

    @ConvertToGoFormats
    @PostMapping("/external/getRecord")
    public Object getRecordList(@RequestBody EccReq eccReq) throws NoSuchAlgorithmException, IOException {

        ClassPathResource resource = new ClassPathResource("json/experience/EccRecordList.json");

        return JsonUtils.getSource().readValue(resource.getStream(), Object.class);
    }

    @PostMapping("/external/getRecordDetail")
    public CommonResult<EccMaintenance> getRecordList(@RequestBody EccOperationDetailReq req) throws NoSuchAlgorithmException, IOException {

        ClassPathResource resource = new ClassPathResource("json/experience/EccRecordDetail.json");

        List<EccMaintenance> eccMaintenances = JsonUtils.getSource().readValue(resource.getStream(), new TypeReference<List<EccMaintenance>>() {
        });

        return CommonResult.suc(
                eccMaintenances.stream()
                        .filter(item -> ObjectUtil.equals(item.getPlanId(), req.getPlanId()))
                        .findFirst()
                        .orElse(null)
        );
    }

    @ConvertToGoFormats
    @PostMapping("/om/report/info/load")
    public CommonResult<Object> getPatrolRecordInfo(@RequestBody PatrolRecordReq patrolRecordReq) {
        return CommonResult.suc(operationMaintenanceService.getPatrolRecordInfo(patrolRecordReq));
    }

}
