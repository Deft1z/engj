package com.kge.energy.crm.resource.controller;

import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.resource.resp.ResourceListResp;
import com.kge.energy.crm.resource.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
