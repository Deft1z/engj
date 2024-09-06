package com.kge.energy.crm.contract.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.contract.req.*;
import com.kge.energy.crm.contract.resp.ScServiceContractResp;
import com.kge.energy.crm.contract.service.ScServiceContractService;
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

    @Operation(summary = "获取服务合同列表")
    @PostMapping(value = "/getPage")
    @ConvertToGoFormats
    public CommonResponse<PageResp<ScServiceContractResp>> getPage(@RequestBody ScServiceContractPageReq req){
        return CommonResponse.suc(scServiceContractService.getPage(req));
    }

    @Operation(summary = "根据当前工单查询相关的合同")
    @PostMapping(value = "/getContractByFormId")
    @ConvertToGoFormats
    public CommonResponse<List<ScServiceContractResp>> getContractByFormId(@RequestBody ScServiceContractDetailReq req){
        return CommonResponse.suc(scServiceContractService.getContractByFormId(req));
    }

    @Operation(summary = "新增合同")
    @PostMapping(value = "/opt/insert")
    @ConvertToGoFormats
    public CommonResponse<Boolean> insert(@RequestBody @Valid ScServiceContractAddReq req){
        return CommonResponse.suc(scServiceContractService.insert(req));
    }

    @Operation(summary = "更新合同项目结束时间")
    @PostMapping(value = "/opt/update")
    @ConvertToGoFormats
    public CommonResponse<Boolean> update(@RequestBody @Valid ScServiceContractProjEndTimeUpdReq req){
        return CommonResponse.suc(scServiceContractService.update(req));
    }

    @Operation(summary = "更新合同项目时间")
    @PostMapping(value = "/projectTime/update")
    @ConvertToGoFormats
    public CommonResponse<Boolean> updateProjTime(@RequestBody @Valid ScServiceContractProjTimeUpdReq req){
        return CommonResponse.suc(scServiceContractService.updateProjTime(req));
    }

    @Operation(summary = "新增用户评价")
    @PostMapping(value = "/evaluate/insert")
    @ConvertToGoFormats
    public CommonResponse<Boolean> addEvaluation(@RequestBody @Valid ScServiceContractEvaAddReq req){
        return CommonResponse.suc(scServiceContractService.addEvaluation(req));
    }

}