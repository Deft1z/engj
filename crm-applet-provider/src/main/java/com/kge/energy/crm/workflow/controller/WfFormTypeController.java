package com.kge.energy.crm.workflow.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.workflow.resp.WfFormTypeTreeResp;
import com.kge.energy.crm.workflow.service.WfFormTypeService;
import com.kge.platform.framework.common.net.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("")
@Tag(name = "工单类型")
@RequiredArgsConstructor
public class WfFormTypeController {

    private final WfFormTypeService wfFormTypeService;

    @Operation(summary = "获取工单服务类型树")
    @PostMapping(value = "/base/service/list")
    @ConvertToGoFormats
    public CommonResult<List<WfFormTypeTreeResp>> getFormTypeTree() {
        return CommonResult.suc(wfFormTypeService.getFormTypeTree());
    }

}
