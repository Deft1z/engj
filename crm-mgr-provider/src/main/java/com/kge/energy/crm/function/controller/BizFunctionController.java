package com.kge.energy.crm.function.controller;

import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.function.req.AddBizFunctionReq;
import com.kge.energy.crm.function.req.BizFunctionListReq;
import com.kge.energy.crm.function.req.DeleteBizFunctionReq;
import com.kge.energy.crm.function.req.UpdateBizFunctionReq;
import com.kge.energy.crm.function.resp.BizFunctionListResp;
import com.kge.energy.crm.function.service.BizFunctionService;
import com.kge.platform.framework.common.net.CommonResult;
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
@Tag(name = "业务功能")
@RestController
@RequestMapping("/bizFunction")
@RequiredArgsConstructor
public class BizFunctionController {

    private final BizFunctionService bizFunctionService;

    @Operation(summary = "获取业务功能配置列表")
    @PostMapping("/list")
    public CommonResult<PageResp<BizFunctionListResp>> list(@Validated @RequestBody BizFunctionListReq req) {
        return CommonResult.suc(bizFunctionService.list(req));
    }

    @Operation(summary = "新增业务功能配置")
    @PostMapping("/add")
    public CommonResult<Boolean> add(@Validated @RequestBody AddBizFunctionReq req) {
        return CommonResult.suc(bizFunctionService.add(req));
    }

    @Operation(summary = "更新业务功能配置")
    @PostMapping("/update")
    public CommonResult<Boolean> update(@Validated @RequestBody UpdateBizFunctionReq req) {
        return CommonResult.suc(bizFunctionService.update(req));
    }

    @Operation(summary = "删除业务功能配置")
    @PostMapping("/delete")
    public CommonResult<Boolean> delete(@Validated @RequestBody DeleteBizFunctionReq req) {
        return CommonResult.suc(bizFunctionService.delete(req));
    }
}
