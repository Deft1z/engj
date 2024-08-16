package com.kge.energy.crm.user.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.user.req.UpdateWxUserReq;
import com.kge.energy.crm.user.service.UserService;
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
@Tag(name = "用户模块")
@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/baseData/userMrg/wxUser/update")
    @ConvertToGoFormats
    public CommonResponse<Boolean> updateWxUser(@Validated @RequestBody UpdateWxUserReq req) {
        return CommonResponse.suc(userService.updateWxUser(req));
    }
}
