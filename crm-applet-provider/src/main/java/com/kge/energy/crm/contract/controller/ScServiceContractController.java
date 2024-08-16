package com.kge.energy.crm.contract.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.contract.req.ScServiceContractReq;
import com.kge.energy.crm.contract.resp.ScServiceContractResp;
import com.kge.energy.crm.contract.service.ScServiceContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workMgr/contract")
@Tag(name = "服务合同")
@RequiredArgsConstructor
public class ScServiceContractController {

    private final ScServiceContractService scServiceContractService;

    @Operation(summary = "获取服务合同列表")
    @PostMapping(value = "/getPage")
    @ConvertToGoFormats
    public CommonResponse<PageResp<ScServiceContractResp>> getPage(@RequestBody ScServiceContractReq req){
        return CommonResponse.suc(scServiceContractService.getPage(req));
    }

}