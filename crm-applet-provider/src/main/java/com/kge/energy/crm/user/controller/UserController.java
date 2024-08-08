package com.kge.energy.crm.user.controller;

import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.resource.resp.ResourceListResp;
import com.kge.energy.crm.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户登录接口
 *
 * @author zqy
 */
@RestController
@RequestMapping("/base/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 当前登录用户菜单
     */
    @PostMapping("/currentUserResources")
    public CommonResponse<ResourceListResp> currentUserResources() {
//        return CommonResponse.suc(userService.currentUserResources());
        return null;
    }
}



