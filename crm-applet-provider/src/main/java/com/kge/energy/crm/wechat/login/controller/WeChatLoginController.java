package com.kge.energy.crm.wechat.login.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.external.wechat.applet.service.WeChatAppletInfraService;
import com.kge.energy.crm.wechat.login.req.GetRecommendQrCodeReq;
import com.kge.energy.crm.wechat.login.req.PhoneNumberReq;
import com.kge.energy.crm.wechat.login.req.WeChatLoginReq;
import com.kge.energy.crm.wechat.login.resp.WeChatLoginResp;
import com.kge.energy.crm.wechat.login.resp.WeChatPhoneNumberResp;
import com.kge.energy.crm.wechat.login.resp.WxAppletRecommendQrCodeResp;
import com.kge.energy.crm.wechat.login.resp.WxLoginUserInfoResp;
import com.kge.energy.crm.wechat.login.service.WeChatLoginService;
import com.kge.platform.framework.common.net.CommonResult;
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

    private final WeChatAppletInfraService weChatAppletInfraService;

    /**
     * 用户登录
     */
    @PostMapping("/wechat/login")
    @ConvertToGoFormats
    public CommonResult<WeChatLoginResp> login(@RequestBody WeChatLoginReq req) {
        return CommonResult.suc(weChatLoginService.login(req));
    }

    /**
     * 获取微信小程序用户的手机号码
     */
    @PostMapping("/baseData/wechat/phoneNumber")
    @ConvertToGoFormats
    public CommonResult<WeChatPhoneNumberResp> phoneNumber(@Validated @RequestBody PhoneNumberReq req) {
        return CommonResult.suc(weChatLoginService.phoneNumber(req));
    }

    /**
     * 获取登陆用户信息
     */
    @PostMapping("/baseData/wechat/userInfo")
    @ConvertToGoFormats
    public CommonResult<WxLoginUserInfoResp> getWxLoginUserInfo() {
        return CommonResult.suc(weChatLoginService.getWxLoginUserInfo());
    }

    /**
     * 获取个人推荐二维码
     */
    @PostMapping("/baseData/wechat/recommendQrCode")
    public CommonResult<WxAppletRecommendQrCodeResp> getWxAppletRecommendQrCode(@Validated @RequestBody GetRecommendQrCodeReq req) {
        return CommonResult.suc(weChatLoginService.getWxAppletRecommendQrCode(req));
    }

    /**
     * 获取个人推荐二维码图片
     */
    @PostMapping("/baseData/wechat/recommendQrCodePng")
    public CommonResult<String> getWxAppletRecommendQrCodePng(@Validated @RequestBody GetRecommendQrCodeReq req) {
        return CommonResult.suc(weChatLoginService.getWxAppletRecommendQrCodePng(req));
    }
}



