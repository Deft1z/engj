package com.kge.energy.crm.contract.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.contract.req.*;
import com.kge.energy.crm.contract.resp.ScServiceContractResp;
import com.kge.energy.crm.contract.service.ScServiceContractService;
import com.kge.energy.crm.workOrder.req.ServiceContractAddReq;
import com.kge.energy.crm.workOrder.req.ServiceContractReq;
import com.kge.energy.crm.workOrder.req.ServiceContractUpdateProjectTimeReq;
import com.kge.energy.crm.workOrder.resp.ServiceContractResp;
import com.kge.energy.crm.workOrder.service.ServiceContractDomainService;
import com.kge.platform.framework.common.net.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/workMgr/contract")
@Tag(name = "服务合同")
@RequiredArgsConstructor
public class ScServiceContractController {

    private final ScServiceContractService scServiceContractService;

    private final ServiceContractDomainService serviceContractCommonService;

    @Operation(summary = "获取服务合同列表")
    @PostMapping(value = "/getPage")
    @ConvertToGoFormats
    public CommonResult<PageResp<ScServiceContractResp>> getPage(@RequestBody ScServiceContractPageReq req) {
        return CommonResult.suc(scServiceContractService.getPage(req));
    }

    @Operation(summary = "根据当前工单查询相关的合同")
    @PostMapping(value = "/getContractByFormId")
    @ConvertToGoFormats
    public CommonResult<List<ServiceContractResp>> getContractByFormId(@RequestBody ServiceContractReq req) {
        return CommonResult.suc(serviceContractCommonService.getServiceContractList(req));
    }

    @Operation(summary = "新增合同")
    @PostMapping(value = "/opt/insert")
    @ConvertToGoFormats
    public CommonResult<Boolean> insert(@RequestBody @Valid ServiceContractAddReq req) {
        return CommonResult.suc(serviceContractCommonService.addServiceContract(req));
    }

    @Operation(summary = "更新合同项目结束时间")
    @PostMapping(value = "/opt/update")
    @ConvertToGoFormats
    public CommonResult<Boolean> update(@RequestBody @Valid ScServiceContractProjEndTimeUpdReq req) {
        return CommonResult.suc(scServiceContractService.update(req));
    }

    /**
     * 小程序端更新项目开工竣工时间
     * @param req
     * @return
     */
    @Operation(summary = "更新合同项目时间")
    @PostMapping(value = "/projectTime/update")
    @ConvertToGoFormats
    public CommonResult<Boolean> updateProjTime(@RequestBody @Valid ServiceContractUpdateProjectTimeReq req) {
        return CommonResult.suc(serviceContractCommonService.updateProjectTime(req));
    }

    @Operation(summary = "新增用户评价")
    @PostMapping(value = "/evaluate/insert")
    @ConvertToGoFormats
    public CommonResult<Boolean> addEvaluation(@RequestBody @Valid ScServiceContractEvaAddReq req) {
        return CommonResult.suc(scServiceContractService.addEvaluation(req));
    }

}
