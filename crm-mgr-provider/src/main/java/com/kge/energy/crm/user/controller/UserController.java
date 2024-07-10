package com.kge.energy.crm.user.controller;

import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.user.req.RoleUserReq;
import com.kge.energy.crm.user.resp.RoleUserResp;
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
@RequestMapping("/baseDataBack/userBackMrg")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 角色ID获取用户
     */
    @PostMapping("/getUserByRoleId")
    public CommonResponse<List<RoleUserResp>> getUserByRoleId(@Validated @RequestBody RoleUserReq req) {

        return CommonResponse.suc(userService.getUserByRoleId(req));
    }

}
