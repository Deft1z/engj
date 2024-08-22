package com.kge.energy.crm.log.controller;

import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.log.req.SysOperateLogListReq;
import com.kge.energy.crm.log.resp.SysOperateLogListResp;
import com.kge.energy.crm.log.service.SysOperateLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangjihua
 */
@Tag(name = "操作日志")
@RestController
@RequestMapping("/sysOperateLog")
@RequiredArgsConstructor
public class SysOperateLogController {

    private final SysOperateLogService sysOperateLogService;

    @Operation(summary = "操作日志列表")
    @PostMapping("/list")
    public CommonResponse<PageResp<SysOperateLogListResp>> list(@Validated @RequestBody SysOperateLogListReq req) {
        return CommonResponse.suc(sysOperateLogService.list(req));
    }
}
