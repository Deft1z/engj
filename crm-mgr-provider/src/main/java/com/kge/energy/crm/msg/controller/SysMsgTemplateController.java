package com.kge.energy.crm.msg.controller;

import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.msg.req.*;
import com.kge.energy.crm.msg.resp.SysMsgTemplateResp;
import com.kge.energy.crm.msg.service.SysMsgTemplateService;
import com.kge.platform.framework.common.net.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 消息模板(SysMsgTemplate)Controller层
 */
@RestController
@RequestMapping("/sysMsgTemplate")
@Tag(name = "消息模板")
@RequiredArgsConstructor
public class SysMsgTemplateController {

    private final SysMsgTemplateService sysMsgTemplateService;

    @Operation(summary = "获取消息模板配置列表")
    @PostMapping("/list")
    public CommonResult<PageResp<SysMsgTemplateResp>> list(@Validated @RequestBody SysMsgTemplateListReq req) {
        return CommonResult.suc(sysMsgTemplateService.list(req));
    }

    @Operation(summary = "新增消息模板配置")
    @PostMapping("/add")
    public CommonResult<Boolean> add(@Validated @RequestBody AddSysMsgTemplateReq req) {
        return CommonResult.suc(sysMsgTemplateService.add(req));
    }

    @Operation(summary = "更新消息模板配置")
    @PostMapping("/update")
    public CommonResult<Boolean> update(@Validated @RequestBody UpdateSysMsgTemplateReq req) {
        return CommonResult.suc(sysMsgTemplateService.update(req));
    }

    @Operation(summary = "删除消息模板配置")
    @PostMapping("/delete")
    public CommonResult<Boolean> delete(@Validated @RequestBody DeleteSysMsgTemplateReq req) {
        return CommonResult.suc(sysMsgTemplateService.delete(req));
    }

    @Operation(summary = "查询单个消息模板配置")
    @PostMapping("/getSysMsgTemplate")
    public CommonResult<SysMsgTemplateResp> delete(@Validated @RequestBody SysMsgTemplateReq req) {
        return CommonResult.suc(sysMsgTemplateService.getSysMsgTemplate(req));
    }

}

