package com.kge.energy.crm.external.wechat.service;

import com.kge.energy.crm.external.wechat.property.WeChatProperties;
import com.kge.energy.crm.external.wechat.resp.WeChatAppletLoginResp;
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
}
