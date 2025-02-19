package com.kge.energy.crm.ai.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.kge.energy.crm.ai.service.AiService;
import com.kge.energy.crm.external.ai.resp.AiAuthResp;
import com.kge.platform.framework.common.net.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai")
@Tag(name = "AI 服务")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @ApiOperationSupport(order = 1)
    @Operation(summary = "获取AI服务（open-webui）的JWT令牌")
    @GetMapping("/getAuthToken")
    public CommonResult<AiAuthResp> getAuthToken() {
        return CommonResult.suc(aiService.getAuthToken());
    }

}

