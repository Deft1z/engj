package com.kge.energy.crm.external.wechat.common.service;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.kge.energy.crm.common.execption.BadException;
import com.kge.energy.crm.external.wechat.applet.req.StableAccessTokenReq;
import com.kge.energy.crm.external.wechat.applet.resp.StableAccessTokenResp;
import com.kge.energy.crm.external.wechat.common.property.WeChatCommonProperties;
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
public class WeChatCommonInfraService {

    private final WeChatCommonProperties weChatCommonProperties;

    private final StringRedisTemplate stringRedisTemplate;

    private static final String ACCESS_TOKEN_CACHE_KEY_PREFIX = "wechat:access_token:";


    /**
     * 获取稳定版接口调用凭据
     */
    public String getAccessToken(String appId, String appSecret) {

        String accessTokenKey = ACCESS_TOKEN_CACHE_KEY_PREFIX + appId;
        String accessToken = stringRedisTemplate.opsForValue().get(accessTokenKey);
        if (StrUtil.isNotBlank(accessToken)) {
            return accessToken;
        }

        StableAccessTokenResp resp = getStableAccessToken(appId, appSecret);
        if (ObjUtil.isNull(resp)) {
            throw new BadException("获取微信调用凭证失败");
        }

        accessToken = resp.getAccessToken();
        // 比微信 token 提前 4 分钟前过期
        int timeout = resp.getExpiresIn() - 4 * 60;

        stringRedisTemplate.opsForValue().set(accessTokenKey, accessToken, timeout, TimeUnit.SECONDS);

        return accessToken;
    }

    /**
     * 获取稳定版接口调用凭据
     * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/mp-access-token/getStableAccessToken.html
     */
    public StableAccessTokenResp getStableAccessToken(String appId, String appSecret) {

        String url = String.format("%s/cgi-bin/stable_token", weChatCommonProperties.getWxUrl());
        StableAccessTokenReq req = new StableAccessTokenReq()
                .setAppid(appId)
                .setSecret(appSecret);

        return RestUtils.postForObject(url, req, StableAccessTokenResp.class);
    }
}
