package com.kge.energy.crm.complain.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.complain.req.ComplainAddReq;
import com.kge.energy.crm.complain.resp.ComplainFormResp;
import com.kge.energy.crm.complain.service.ComplainService;
import com.kge.energy.crm.workflow.req.WfFormReq;
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

    @Operation(summary = "获取工单列表")
    @PostMapping(value = "/getByPage")
    @ConvertToGoFormats
    public CommonResponse<PageResp<ComplainFormResp>> getByPage(@RequestBody @Valid WfFormReq req){
        return CommonResponse.suc(complainService.getByPage(req));
    }
    @Operation(summary = "用户提出投诉")
    @PostMapping(value = "/opt/insert")
    @ConvertToGoFormats
    public CommonResponse<Boolean> insert(@RequestBody @Valid ComplainAddReq req){
        return CommonResponse.suc(complainService.insert(req));
    }

}