package com.kge.energy.crm.wechat.login.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.wechat.login.req.PhoneNumberReq;
import com.kge.energy.crm.wechat.login.req.WeChatLoginReq;
import com.kge.energy.crm.wechat.login.resp.WeChatLoginResp;
import com.kge.energy.crm.wechat.login.resp.WeChatPhoneNumberResp;
import com.kge.energy.crm.wechat.login.resp.WxLoginUserInfoResp;
import com.kge.energy.crm.wechat.login.service.WeChatLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户登录接口
 *
 * @author zqy
 */
@RestController
@RequestMapping()
@RequiredArgsConstructor
public class WeChatLoginController {

    private final WeChatLoginService weChatLoginService;

    /**
     * 用户登录
     */
    @PostMapping("/wechat/login")
    @ConvertToGoFormats
    public CommonResponse<WeChatLoginResp> login(@RequestBody WeChatLoginReq req) {
        return CommonResponse.suc(weChatLoginService.login(req));
    }

    /**
     * 获取微信小程序用户的手机号码
     */
    @PostMapping("/baseData/wechat/phoneNumber")
    @ConvertToGoFormats
    public CommonResponse<WeChatPhoneNumberResp> phoneNumber(@Validated @RequestBody PhoneNumberReq req) {
        return CommonResponse.suc(weChatLoginService.phoneNumber(req));
    }

    /**
     * 获取登陆用户信息
     */
    @PostMapping("/baseData/wechat/userInfo")
    @ConvertToGoFormats
    public CommonResponse<WxLoginUserInfoResp> getWxLoginUserInfo() {
        return CommonResponse.suc(weChatLoginService.getWxLoginUserInfo());
    }


}



