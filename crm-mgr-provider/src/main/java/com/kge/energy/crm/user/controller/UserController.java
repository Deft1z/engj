package com.kge.energy.crm.user.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.user.req.*;
import com.kge.energy.crm.user.resp.*;
import com.kge.energy.crm.user.service.UserService;
import com.kge.platform.framework.common.net.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangjihua
 */
@Tag(name = "用户模块")
@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 角色ID获取用户
     */
    @ConvertToGoFormats
    @PostMapping("/baseDataBack/userBackMrg/getUserByRoleId")
    public CommonResult<List<RoleUserResp>> getUserByRoleId(@Validated @RequestBody RoleUserReq req) {

        return CommonResult.suc(userService.getUserByRoleId(req));
    }

    /**
     * 获取当前用户加密用的盐值
     */
    @ConvertToGoFormats
    @PostMapping("/base/user/salt")
    public CommonResult<String> userSalt(@Validated @RequestBody UserSaltReq req) {
        return CommonResult.suc(userService.userSalt(req));
    }

    /**
     * PC端账号密码登录
     */
    @ConvertToGoFormats
    @PostMapping("/base/user/login")
    public CommonResult<UserLoginResp> userLogin(@Validated @RequestBody UserLoginReq req) {
        return CommonResult.suc(userService.userLogin(req));
    }

    /**
     * 获取用户信息
     */
    @ConvertToGoFormats
    @PostMapping("/baseDataBack/userBackMrg/currentUserInfo")
    public CommonResult<CurrentUserInfoResp> currentUserInfo() {
        return CommonResult.suc(userService.currentUserInfo());
    }

    /**
     * 获取小程序用户列表
     */
    @ConvertToGoFormats
    @PostMapping("/baseDataBack/userBackMrg/wxUser/load")
    public CommonResult<PageResp<WxUserListResp>> findAppletUser(@RequestBody WxUserListReq req) {
        return CommonResult.suc(userService.findAppletUser(req));
    }

    @Operation(summary = "获取租户或部门下的用户列表")
    @PostMapping("/user/list")
    public CommonResult<PageResp<UserListResp>> list(@Validated @RequestBody UserListReq req) {
        return CommonResult.suc(userService.list(req));
    }

    @Operation(summary = "获取角色的用户列表")
    @PostMapping("/user/listByRole")
    public CommonResult<PageResp<UserListResp>> listByRole(@Validated @RequestBody UserListByRoleReq req) {
        return CommonResult.suc(userService.listByRole(req));
    }

    @Operation(summary = "用户详情")
    @GetMapping("/user/detail/{userId}")
    public CommonResult<UserDetailResp> detail(@PathVariable("userId") Integer userId) {
        return CommonResult.suc(userService.detail(userId));
    }

    @Operation(summary = "新增用户")
    @PostMapping("/user/add")
    public CommonResult<Boolean> add(@Validated @RequestBody AddUserReq req) {
        return CommonResult.suc(userService.add(req));
    }

    @Operation(summary = "编辑用户")
    @PostMapping("/user/update")
    public CommonResult<Boolean> update(@Validated @RequestBody UpdateUserReq req) {
        return CommonResult.suc(userService.update(req));
    }

    @Operation(summary = "删除用户")
    @PostMapping("/user/delete")
    public CommonResult<Boolean> delete(@Validated @RequestBody DeleteUserReq req) {
        return CommonResult.suc(userService.delete(req));
    }

    @Operation(summary = "分配用户角色")
    @PostMapping("/user/assignRole")
    public CommonResult<Boolean> assignRole(@Validated @RequestBody AssignUserRoleReq req) {
        return CommonResult.suc(userService.assignRole(req));
    }

    @Operation(summary = "移除用户角色")
    @PostMapping("/user/removeRole")
    public CommonResult<Boolean> removeRole(@Validated @RequestBody RemoveUserRoleReq req) {
        return CommonResult.suc(userService.removeRole(req));
    }

    @Operation(summary = "重置密码")
    @PostMapping("/user/resetPwd")
    public CommonResult<Boolean> resetPwd(@Validated @RequestBody ResetPwdReq req) {
        return CommonResult.suc(userService.resetPwd(req));
    }

}
