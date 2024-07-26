package com.kge.energy.crm.external.wechat.applet.service;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.kge.energy.crm.common.execption.BadException;
import com.kge.energy.crm.external.wechat.applet.property.WeChatAppletProperties;
import com.kge.energy.crm.external.wechat.applet.req.GetUserPhoneNumberReq;
import com.kge.energy.crm.external.wechat.applet.req.SendSubscribeReq;
import com.kge.energy.crm.external.wechat.applet.req.StableAccessTokenReq;
import com.kge.energy.crm.external.wechat.applet.resp.GetUserPhoneNumberResp;
import com.kge.energy.crm.external.wechat.applet.resp.LoginResp;
import com.kge.energy.crm.external.wechat.applet.resp.SendSubscribeResp;
import com.kge.energy.crm.external.wechat.applet.resp.StableAccessTokenResp;
import com.kge.platform.framework.web.util.RestUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatAppletInfraService {

    private final WeChatAppletProperties wechatAppletProperties;

    private final StringRedisTemplate stringRedisTemplate;

    private static final String ACCESS_TOKEN_CACHE_KEY = "ntcrm:applet:access_token";

    /**
     * 微信小程序登录
     * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-login/code2Session.html
     */
    public LoginResp appletLogin(String jsCode) {

        String url = String.format("%s/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                wechatAppletProperties.getWxUrl(), wechatAppletProperties.getAppId(), wechatAppletProperties.getAppSecret(), jsCode);

        return RestUtils.instance().getForObject(url, LoginResp.class);
    }


    /**
     * 获取稳定版接口调用凭据
     */
    public String getAccessToken() {

        String accessToken = stringRedisTemplate.opsForValue().get(ACCESS_TOKEN_CACHE_KEY);
        if (StrUtil.isNotBlank(accessToken)) {
            return accessToken;
        }

        StableAccessTokenResp resp = getStableAccessToken();
        if (ObjUtil.isNull(resp)) {
            throw new BadException("获取小程序Token失败");
        }

        accessToken = resp.getAccessToken();
        // 比微信 token 提前 4 分钟前过期
        int timeout = resp.getExpiresIn() - 4 * 60;

        stringRedisTemplate.opsForValue().set(ACCESS_TOKEN_CACHE_KEY, accessToken, timeout, TimeUnit.SECONDS);

        return accessToken;
    }

    /**
     * 获取稳定版接口调用凭据
     * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/mp-access-token/getStableAccessToken.html
     */
    public StableAccessTokenResp getStableAccessToken() {

        String url = String.format("%s/cgi-bin/stable_token", wechatAppletProperties.getWxUrl());
        StableAccessTokenReq req = new StableAccessTokenReq()
                .setAppid(wechatAppletProperties.getAppId())
                .setSecret(wechatAppletProperties.getAppSecret());

        return RestUtils.postForObject(url, req, StableAccessTokenResp.class);
    }

    /**
     * 通过获取用户手机号
     * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-info/phone-number/getPhoneNumber.html
     */
    public GetUserPhoneNumberResp getUserPhoneNumber(String jsCode, String openId) {

        String url = String.format("%s/wxa/business/getuserphonenumber?access_token=%s", wechatAppletProperties.getWxUrl(), getAccessToken());

        GetUserPhoneNumberReq req = new GetUserPhoneNumberReq()
                .setCode(jsCode)
                .setOpenId(openId);

        return RestUtils.postForObject(url, req, GetUserPhoneNumberResp.class);
    }

    /**
     * 发送订阅消息
     * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/mp-message-management/subscribe-message/sendMessage.html
     */
    public SendSubscribeResp sendSubscribe(SendSubscribeReq req) {

        String url = String.format("%s/cgi-bin/message/subscribe/send?access_token=%s", wechatAppletProperties.getWxUrl(), getAccessToken());

        return RestUtils.postForObject(url, req, SendSubscribeResp.class);
    }

}
