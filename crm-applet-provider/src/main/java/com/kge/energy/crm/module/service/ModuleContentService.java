package com.kge.energy.crm.module.service;

import cn.hutool.core.util.StrUtil;
import com.kge.energy.crm.external.wechat.officialaccount.req.GetPublishArticleReq;
import com.kge.energy.crm.external.wechat.officialaccount.resp.GetPublishArticleResp;
import com.kge.energy.crm.external.wechat.officialaccount.service.WeChatOfficialAccountInfraService;
import com.kge.platform.framework.web.util.JsonUtils;
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
public class ModuleContentService {

    private final WeChatOfficialAccountInfraService weChatOfficialAccountInfraService;

    private final StringRedisTemplate stringRedisTemplate;

    private static final String NFTZ_OFFICIAL_ACCOUNT_ARTICLE_CACHE_KEY = "nftz:official_account:article";

    /**
     * 获取南投集团公众号推文新闻
     */
    public GetPublishArticleResp getNews(GetPublishArticleReq req) {

        String news = stringRedisTemplate.opsForValue().get(NFTZ_OFFICIAL_ACCOUNT_ARTICLE_CACHE_KEY);

        if (StrUtil.isNotBlank(news)) {
            return JsonUtils.deserialize(news, GetPublishArticleResp.class);
        }

        GetPublishArticleResp resp = weChatOfficialAccountInfraService.getPublishArticle(req);

        stringRedisTemplate.opsForValue()
                .set(NFTZ_OFFICIAL_ACCOUNT_ARTICLE_CACHE_KEY, JsonUtils.serialize(resp), 4, TimeUnit.HOURS);

        return resp;
    }
}
