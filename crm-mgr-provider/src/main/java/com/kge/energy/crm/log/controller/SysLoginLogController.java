package com.kge.energy.crm.log.controller;

import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.log.req.SysLoginLogListReq;
import com.kge.energy.crm.log.resp.SysLoginLogListResp;
import com.kge.energy.crm.log.service.SysLoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "登录日志")
@RestController
@RequestMapping("/sysLoginLog")
@RequiredArgsConstructor
public class SysLoginLogController {

    private final SysLoginLogService sysLoginLogService;

    @Operation(summary = "登录日志列表")
    @PostMapping("/list")
    public CommonResponse<PageResp<SysLoginLogListResp>> list(@Validated @RequestBody SysLoginLogListReq req) {
        return CommonResponse.suc(sysLoginLogService.list(req));
    }

}
