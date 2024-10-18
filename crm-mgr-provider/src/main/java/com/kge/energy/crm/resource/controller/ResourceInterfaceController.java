package com.kge.energy.crm.resource.controller;

import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.resource.req.ResourceInterfaceAddReq;
import com.kge.energy.crm.resource.req.ResourceInterfaceDeleteReq;
import com.kge.energy.crm.resource.req.ResourceInterfaceListReq;
import com.kge.energy.crm.resource.req.ResourceInterfaceUpdateReq;
import com.kge.energy.crm.resource.resp.ResourceInterfaceResp;
import com.kge.energy.crm.resource.service.ResourceInterfaceService;
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
@Tag(name = "资源接口")
@RestController
@RequestMapping("/resource/interface")
@RequiredArgsConstructor
public class ResourceInterfaceController {

    private final ResourceInterfaceService resourceInterfaceService;

    @Operation(summary = "资源接口列表")
    @PostMapping("/list")
    public CommonResult<PageResp<ResourceInterfaceResp>> list(@Validated @RequestBody ResourceInterfaceListReq req) {
        return CommonResult.suc(resourceInterfaceService.list(req));
    }

    @Operation(summary = "新增接口")
    @PostMapping("/add")
    public CommonResult<Boolean> add(@Validated @RequestBody ResourceInterfaceAddReq req) {
        return CommonResult.suc(resourceInterfaceService.add(req));
    }

    @Operation(summary = "更新接口")
    @PostMapping("/update")
    public CommonResult<Boolean> update(@Validated @RequestBody ResourceInterfaceUpdateReq req) {
        return CommonResult.suc(resourceInterfaceService.update(req));
    }

    @Operation(summary = "删除接口")
    @PostMapping("/delete")
    public CommonResult<Boolean> delete(@Validated @RequestBody ResourceInterfaceDeleteReq req) {
        return CommonResult.suc(resourceInterfaceService.delete(req));
    }
}
