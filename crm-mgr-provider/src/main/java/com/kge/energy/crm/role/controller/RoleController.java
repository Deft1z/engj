package com.kge.energy.crm.role.controller;

import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.role.req.*;
import com.kge.energy.crm.role.resp.RoleListResp;
import com.kge.energy.crm.role.resp.RoleResourceResp;
import com.kge.energy.crm.role.resp.UserRoleResp;
import com.kge.energy.crm.role.service.RoleService;
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
@Tag(name = "角色管理")
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "角色列表")
    @PostMapping("/list")
    public CommonResult<PageResp<RoleListResp>> list(@Validated @RequestBody RoleListReq req) {
        return CommonResult.suc(roleService.list(req));
    }

    @Operation(summary = "新增角色")
    @PostMapping("/add")
    public CommonResult<Boolean> add(@Validated @RequestBody AddRoleReq req) {
        return CommonResult.suc(roleService.add(req));
    }

    @Operation(summary = "编辑角色")
    @PostMapping("/update")
    public CommonResult<Boolean> update(@Validated @RequestBody UpdateRoleReq req) {
        return CommonResult.suc(roleService.update(req));
    }

    @Operation(summary = "删除角色")
    @PostMapping("/delete")
    public CommonResult<Boolean> delete(@Validated @RequestBody DeleteRoleReq req) {
        return CommonResult.suc(roleService.delete(req));
    }

    @Operation(summary = "角色已关联菜单")
    @PostMapping("/roleResource")
    public CommonResult<RoleResourceResp> roleResource(@Validated @RequestBody RoleResourceReq req) {
        return CommonResult.suc(roleService.roleResource(req));
    }

    @Operation(summary = "给角色关联菜单")
    @PostMapping("/assignResource")
    public CommonResult<Boolean> assignResource(@Validated @RequestBody RoleAssignResourceReq req) {
        return CommonResult.suc(roleService.assignResource(req));
    }

    @Operation(summary = "获取用户角色")
    @PostMapping("/userRole")
    public CommonResult<UserRoleResp> userRole(@Validated @RequestBody UserRoleReq req) {
        return CommonResult.suc(roleService.userRole(req));
    }
}
