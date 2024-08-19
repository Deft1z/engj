package com.kge.energy.crm.user.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.user.req.*;
import com.kge.energy.crm.user.resp.*;
import com.kge.energy.crm.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public CommonResponse<List<RoleUserResp>> getUserByRoleId(@Validated @RequestBody RoleUserReq req) {

        return CommonResponse.suc(userService.getUserByRoleId(req));
    }

    /**
     * 获取当前用户加密用的盐值
     */
    @ConvertToGoFormats
    @PostMapping("/base/user/salt")
    public CommonResponse<String> userSalt(@Validated @RequestBody UserSaltReq req) {
        return CommonResponse.suc(userService.userSalt(req));
    }

    /**
     * PC端账号密码登录
     */
    @ConvertToGoFormats
    @PostMapping("/base/user/login")
    public CommonResponse<UserLoginResp> userLogin(@Validated @RequestBody UserLoginReq req) {
        return CommonResponse.suc(userService.userLogin(req));
    }

    /**
     * 获取用户信息
     */
    @ConvertToGoFormats
    @PostMapping("/baseDataBack/userBackMrg/currentUserInfo")
    public CommonResponse<CurrentUserInfoResp> currentUserInfo() {
        return CommonResponse.suc(userService.currentUserInfo());
    }

    /**
     * 获取小程序用户列表
     */
    @ConvertToGoFormats
    @PostMapping("/baseDataBack/userBackMrg/wxUser/load")
    public CommonResponse<WxUserListResp> currentWxUserList(@RequestBody WxUserListReq req) {
        return CommonResponse.suc(userService.findWxUserList(req));
    }

    @Operation(summary = "获取租户或部门下的用户列表")
    @PostMapping("/user/list")
    public CommonResponse<PageResp<UserListResp>> list(@Validated @RequestBody UserListReq req) {
        return CommonResponse.suc(userService.list(req));
    }

    @Operation(summary = "新增用户")
    @PostMapping("/user/add")
    public CommonResponse<Boolean> add(@Validated @RequestBody AddUserReq req) {
        return CommonResponse.suc(userService.add(req));
    }

    @Operation(summary = "编辑用户")
    @PostMapping("/user/update")
    public CommonResponse<Boolean> update(@Validated @RequestBody UpdateUserReq req) {
        return CommonResponse.suc(userService.update(req));
    }

    @Operation(summary = "删除用户")
    @PostMapping("/user/delete")
    public CommonResponse<Boolean> delete(@Validated @RequestBody DeleteUserReq req) {
        return CommonResponse.suc(userService.delete(req));
    }

    @Operation(summary = "分配用户角色")
    @PostMapping("/user/assignRole")
    public CommonResponse<Boolean> assignRole(@Validated @RequestBody AssignUserRoleReq req) {
        return CommonResponse.suc(userService.assignRole(req));
    }

    @Operation(summary = "重置密码")
    @PostMapping("/user/resetPwd")
    public CommonResponse<Boolean> resetPwd(@Validated @RequestBody ResetPwdReq req) {
        return CommonResponse.suc(userService.resetPwd(req));
    }

}
