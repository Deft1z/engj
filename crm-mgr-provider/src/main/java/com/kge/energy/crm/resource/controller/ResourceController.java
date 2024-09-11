package com.kge.energy.crm.resource.controller;

import com.kge.energy.crm.resource.req.AddResourceReq;
import com.kge.energy.crm.resource.req.DeleteResourceReq;
import com.kge.energy.crm.resource.req.SystemResourceReq;
import com.kge.energy.crm.resource.req.UpdateResourceReq;
import com.kge.energy.crm.resource.resp.ResourceListResp;
import com.kge.energy.crm.resource.service.ResourceService;
import com.kge.platform.framework.common.net.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author wangjihua
 */
@Tag(name = "菜单资源")
@RestController
@RequestMapping("/resource")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @Operation(summary = "当前登录用户菜单")
    @GetMapping("/currentUserResource")
    public CommonResult<ResourceListResp> currentUserResource() {
        return CommonResult.suc(resourceService.currentUserResource());
    }

    @Operation(summary = "系统所有菜单资源")
    @PostMapping("/systemResources")
    public CommonResult<ResourceListResp> systemResources(@Validated @RequestBody SystemResourceReq req) {
        return CommonResult.suc(resourceService.systemResources(req));
    }

    @Operation(summary = "新增菜单")
    @PostMapping("/add")
    public CommonResult<Boolean> add(@Validated @RequestBody AddResourceReq req) {
        return CommonResult.suc(resourceService.add(req));
    }

    @Operation(summary = "编辑菜单")
    @PostMapping("/update")
    public CommonResult<Boolean> update(@Validated @RequestBody UpdateResourceReq req) {
        return CommonResult.suc(resourceService.update(req));
    }

    @Operation(summary = "删除菜单")
    @PostMapping("/delete")
    public CommonResult<Boolean> delete(@Validated @RequestBody DeleteResourceReq req) {
        return CommonResult.suc(resourceService.delete(req));
    }

}
