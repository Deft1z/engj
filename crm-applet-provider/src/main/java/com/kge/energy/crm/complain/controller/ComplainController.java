package com.kge.energy.crm.complain.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.complain.req.ComplainAddReq;
import com.kge.energy.crm.complain.req.ComplainDetailReq;
import com.kge.energy.crm.complain.resp.ComplainDetailResp;
import com.kge.energy.crm.complain.resp.ComplainFormResp;
import com.kge.energy.crm.complain.service.ComplainDomainService;
import com.kge.energy.crm.complain.service.ComplainService;
import com.kge.energy.crm.workorder.req.WfFormPageReq;
import com.kge.platform.framework.common.net.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workMgr/complain")
@Tag(name = "投诉建议")
@RequiredArgsConstructor
public class ComplainController {

    private final ComplainService complainService;

    private final ComplainDomainService complainDomainService;

    @Operation(summary = "获取投诉列表")
    @PostMapping(value = "/getByPage")
    @ConvertToGoFormats
    public CommonResult<PageResp<ComplainFormResp>> getByPage(@RequestBody @Valid WfFormPageReq req) {
        return CommonResult.suc(complainService.getByPage(req));
    }

    @Operation(summary = "获取投诉详情")
    @PostMapping(value = "/getComplainDetail")
    @ConvertToGoFormats
    public CommonResult<ComplainDetailResp> getComplainDetail(@RequestBody @Valid ComplainDetailReq req) {
        return CommonResult.suc(complainDomainService.getComplainDetail(req));
    }

    @Operation(summary = "用户提出投诉")
    @PostMapping(value = "/opt/insert")
    public CommonResult<Boolean> insert(@RequestBody @Valid ComplainAddReq req) {
        return CommonResult.suc(complainService.insert(req));
    }

}
