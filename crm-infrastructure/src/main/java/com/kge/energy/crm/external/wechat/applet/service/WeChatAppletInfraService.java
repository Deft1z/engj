package com.kge.energy.crm.external.wechat.applet.service;

import com.kge.energy.crm.external.wechat.applet.property.WeChatAppletProperties;
import com.kge.energy.crm.external.wechat.applet.req.GetUserPhoneNumberReq;
import com.kge.energy.crm.external.wechat.applet.req.SendSubscribeReq;
import com.kge.energy.crm.external.wechat.applet.resp.GetUserPhoneNumberResp;
import com.kge.energy.crm.external.wechat.applet.resp.LoginResp;
import com.kge.energy.crm.external.wechat.applet.resp.SendSubscribeResp;
import com.kge.energy.crm.external.wechat.common.service.WeChatCommonInfraService;
import com.kge.platform.framework.web.util.JsonUtils;
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
public class WeChatAppletInfraService {

    private final WeChatAppletProperties wechatAppletProperties;

    private final WeChatCommonInfraService weChatCommonInfraService;

    /**
     * 获取稳定版接口调用凭据
     */
    public String getAccessToken() {

        return weChatCommonInfraService.getAccessToken(
                wechatAppletProperties.getAppId(), wechatAppletProperties.getAppSecret()
        );
    }

    /**
     * 微信小程序登录
     * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-login/code2Session.html
     * todo：现在接口只返回了  {"session_key":"VI6GJ52tcpCQx9eSpLPZlA==","openid":"ocgqB6988rYAugtnawmR6RE2YavE"}，官网更新不及时
     */
    public LoginResp appletLogin(String jsCode) {

        String url = String.format("%s/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                wechatAppletProperties.getWxUrl(), wechatAppletProperties.getAppId(), wechatAppletProperties.getAppSecret(), jsCode);

        String result = RestUtils.instance().getForObject(url, String.class);
        return JsonUtils.deserialize(result, LoginResp.class);
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
