package com.kge.energy.crm.user.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.user.req.RoleUserReq;
import com.kge.energy.crm.user.req.UserLoginReq;
import com.kge.energy.crm.user.req.UserSaltReq;
import com.kge.energy.crm.user.resp.CurrentUserInfoResp;
import com.kge.energy.crm.user.resp.RoleUserResp;
import com.kge.energy.crm.user.resp.UserLoginResp;
import com.kge.energy.crm.user.service.UserService;
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



}
