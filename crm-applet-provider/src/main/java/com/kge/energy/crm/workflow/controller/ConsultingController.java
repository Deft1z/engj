package com.kge.energy.crm.workflow.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.workflow.req.ConsultingAddReq;
import com.kge.energy.crm.workflow.service.ConsultingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workMgr/consulting")
@Tag(name = "业务工单")
@RequiredArgsConstructor
public class ConsultingController {

    private final ConsultingService consultingService;

    @Operation(summary = "创建业务工单")
    @PostMapping(value = "/opt/insert")
    @ConvertToGoFormats
    public CommonResponse<Boolean> save(@RequestBody @Valid ConsultingAddReq req){
        return CommonResponse.suc(consultingService.save(req));
    }



}