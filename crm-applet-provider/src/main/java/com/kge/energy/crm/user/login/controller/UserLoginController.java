package com.kge.energy.crm.user.login.controller;

import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.user.login.req.UserLoginReq;
import com.kge.energy.crm.user.login.resp.UserLoginResp;
import com.kge.energy.crm.user.login.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户登录接口
 * @author zqy
 */
@RestController
@RequestMapping("/base/user")
@RequiredArgsConstructor
public class UserLoginController {

    private final UserLoginService userLoginService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public CommonResponse<List<UserLoginResp>> list(@RequestBody UserLoginReq req) {
        return CommonResponse.suc(userLoginService.list(req));
    }
}



