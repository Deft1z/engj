package com.kge.energy.crm.resource.controller;

import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.resource.req.ResourceInterfaceAddReq;
import com.kge.energy.crm.resource.req.ResourceInterfaceDeleteReq;
import com.kge.energy.crm.resource.req.ResourceInterfaceListReq;
import com.kge.energy.crm.resource.req.ResourceInterfaceUpdateReq;
import com.kge.energy.crm.resource.resp.ResourceInterfaceResp;
import com.kge.energy.crm.resource.service.ResourceInterfaceService;
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
    public CommonResponse<PageResp<ResourceInterfaceResp>> list(@Validated @RequestBody ResourceInterfaceListReq req) {
        return CommonResponse.suc(resourceInterfaceService.list(req));
    }

    @Operation(summary = "新增接口")
    @PostMapping("/add")
    public CommonResponse<Boolean> add(@Validated @RequestBody ResourceInterfaceAddReq req) {
        return CommonResponse.suc(resourceInterfaceService.add(req));
    }

    @Operation(summary = "更新接口")
    @PostMapping("/update")
    public CommonResponse<Boolean> update(@Validated @RequestBody ResourceInterfaceUpdateReq req) {
        return CommonResponse.suc(resourceInterfaceService.update(req));
    }

    @Operation(summary = "删除接口")
    @PostMapping("/delete")
    public CommonResponse<Boolean> delete(@Validated @RequestBody ResourceInterfaceDeleteReq req) {
        return CommonResponse.suc(resourceInterfaceService.delete(req));
    }
}
