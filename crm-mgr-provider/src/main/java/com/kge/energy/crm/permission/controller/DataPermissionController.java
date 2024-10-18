package com.kge.energy.crm.permission.controller;

import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.permission.req.AddDataPermissionReq;
import com.kge.energy.crm.permission.req.DataPermissionListReq;
import com.kge.energy.crm.permission.req.DeleteDataPermissionReq;
import com.kge.energy.crm.permission.req.UpdateDataPermissionReq;
import com.kge.energy.crm.permission.resp.DataPermissionListResp;
import com.kge.energy.crm.permission.service.DataPermissionService;
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
@Tag(name = "数据权限配置")
@RestController
@RequestMapping("/dataPermission")
@RequiredArgsConstructor
public class DataPermissionController {

    private final DataPermissionService dataPermissionService;

    @Operation(summary = "获取业务数据权限配置列表")
    @PostMapping("/list")
    public CommonResult<PageResp<DataPermissionListResp>> list(@Validated @RequestBody DataPermissionListReq req) {
        return CommonResult.suc(dataPermissionService.list(req));
    }

    @Operation(summary = "新增业务数据权限配置")
    @PostMapping("/add")
    public CommonResult<Boolean> add(@Validated @RequestBody AddDataPermissionReq req) {
        return CommonResult.suc(dataPermissionService.add(req));
    }

    @Operation(summary = "更新业务数据权限配置")
    @PostMapping("/update")
    public CommonResult<Boolean> update(@Validated @RequestBody UpdateDataPermissionReq req) {
        return CommonResult.suc(dataPermissionService.update(req));
    }

    @Operation(summary = "删除业务数据权限配置")
    @PostMapping("/delete")
    public CommonResult<Boolean> delete(@Validated @RequestBody DeleteDataPermissionReq req) {
        return CommonResult.suc(dataPermissionService.delete(req));
    }
}
