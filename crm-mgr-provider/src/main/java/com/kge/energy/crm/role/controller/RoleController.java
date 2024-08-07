package com.kge.energy.crm.role.controller;

import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.role.req.*;
import com.kge.energy.crm.role.resp.RoleListResp;
import com.kge.energy.crm.role.resp.RoleResourceResp;
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

    @Operation(summary = "角色列表")
    @PostMapping("/list")
    public CommonResponse<RoleListResp> list(@Validated @RequestBody RoleListReq req) {
        return CommonResponse.suc(null);
    }

    @Operation(summary = "新增角色")
    @PostMapping("/add")
    public CommonResponse<Boolean> add(@Validated @RequestBody AddRoleReq req) {
        return CommonResponse.suc(true);
    }

    @Operation(summary = "编辑角色")
    @PostMapping("/update")
    public CommonResponse<Boolean> update(@Validated @RequestBody UpdateRoleReq req) {
        return CommonResponse.suc(true);
    }

    @Operation(summary = "删除角色")
    @PostMapping("/delete")
    public CommonResponse<Boolean> delete(@Validated @RequestBody DeleteRoleReq req) {
        return CommonResponse.suc(true);
    }

    @Operation(summary = "角色已关联菜单")
    @PostMapping("/roleResource")
    public CommonResponse<RoleResourceResp> delete(@Validated @RequestBody RoleResourceReq req) {
        return CommonResponse.suc(null);
    }
}
