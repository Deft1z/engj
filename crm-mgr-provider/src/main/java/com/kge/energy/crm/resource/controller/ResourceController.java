package com.kge.energy.crm.resource.controller;

import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.resource.req.AddResourceReq;
import com.kge.energy.crm.resource.req.DeleteResourceReq;
import com.kge.energy.crm.resource.req.SystemResourceReq;
import com.kge.energy.crm.resource.req.UpdateResourceReq;
import com.kge.energy.crm.resource.resp.ResourceListResp;
import com.kge.energy.crm.resource.service.ResourceService;
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
    public CommonResponse<ResourceListResp> currentUserResource() {
        return CommonResponse.suc(resourceService.currentUserResource());
    }

    @Operation(summary = "系统所有菜单资源")
    @PostMapping("/systemResources")
    public CommonResponse<ResourceListResp> systemResources(@Validated @RequestBody SystemResourceReq req) {
        return CommonResponse.suc(resourceService.systemResources(req));
    }

    @Operation(summary = "新增菜单")
    @PostMapping("/add")
    public CommonResponse<Boolean> add(@Validated @RequestBody AddResourceReq req) {
        return CommonResponse.suc(true);
    }

    @Operation(summary = "编辑菜单")
    @PostMapping("/update")
    public CommonResponse<Boolean> update(@Validated @RequestBody UpdateResourceReq req) {
        return CommonResponse.suc(true);
    }

    @Operation(summary = "删除菜单")
    @PostMapping("/delete")
    public CommonResponse<Boolean> delete(@Validated @RequestBody DeleteResourceReq req) {
        return CommonResponse.suc(true);
    }

}
