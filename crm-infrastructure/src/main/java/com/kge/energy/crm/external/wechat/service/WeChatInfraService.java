package com.kge.energy.crm.external.wechat.service;

import com.kge.energy.crm.external.wechat.property.WeChatProperties;
import com.kge.energy.crm.external.wechat.req.WeChatAppletGetUserPhoneNumberReq;
import com.kge.energy.crm.external.wechat.req.WeChatAppletStableAccessTokenReq;
import com.kge.energy.crm.external.wechat.resp.WeChatAppletGetUserPhoneNumberResp;
import com.kge.energy.crm.external.wechat.resp.WeChatAppletLoginResp;
import com.kge.energy.crm.external.wechat.resp.WeChatAppletStableAccessTokenResp;
import com.kge.platform.framework.web.util.RestUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatInfraService {

    private final WeChatProperties wechatProperties;

    /**
     * 微信小程序登录
     * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-login/code2Session.html
     */
    public WeChatAppletLoginResp appletLogin(String jsCode) {

        String url = String.format("%s/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                wechatProperties.getWxUrl(), wechatProperties.getAppId(), wechatProperties.getAppSecret(), jsCode);

        return RestUtils.instance().getForObject(url, WeChatAppletLoginResp.class);
    }

    /**
     * 获取稳定版接口调用凭据
     * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/mp-access-token/getStableAccessToken.html
     */
    public WeChatAppletStableAccessTokenResp getStableAccessToken() {

        String url = "https://api.weixin.qq.com/cgi-bin/stable_token";
        WeChatAppletStableAccessTokenReq req = new WeChatAppletStableAccessTokenReq()
                .setAppid(wechatProperties.getAppId())
                .setSecret(wechatProperties.getAppSecret());

        return RestUtils.postForObject(url, req, WeChatAppletStableAccessTokenResp.class);
    }

    /**
     * 通过获取用户手机号
     * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-info/phone-number/getPhoneNumber.html
     */
    public WeChatAppletGetUserPhoneNumberResp getUserPhoneNumber(String accessToken, String jsCode, String openId) {

        String url = String.format("%s/wxa/business/getuserphonenumber?access_token=%s",
                wechatProperties.getWxUrl(), accessToken);

        WeChatAppletGetUserPhoneNumberReq req = new WeChatAppletGetUserPhoneNumberReq()
                .setCode(jsCode)
                .setOpenId(openId);

        return RestUtils.postForObject(url, req, WeChatAppletGetUserPhoneNumberResp.class);
    }
}
