package com.kge.energy.crm.wechat.login.controller;

import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.wechat.login.req.WechatLoginReq;
import com.kge.energy.crm.wechat.login.resp.WechatLoginResp;
import com.kge.energy.crm.wechat.login.service.WechatLoginService;
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
@RequestMapping("/wechat")
@RequiredArgsConstructor
public class WechatLoginController {

    private final WechatLoginService wechatLoginService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public CommonResponse<List<WechatLoginResp>> login(@RequestBody WechatLoginReq req) {
        return CommonResponse.suc(wechatLoginService.login(req));
    }
}



