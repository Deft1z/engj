package com.kge.energy.crm.external.wechat.officialaccount.service;

import cn.hutool.http.HttpUtil;
import com.kge.energy.crm.external.wechat.common.service.WeChatCommonInfraService;
import com.kge.energy.crm.external.wechat.officialaccount.property.WeChatOfficialAccountProperties;
import com.kge.energy.crm.external.wechat.officialaccount.req.GetPublishArticleReq;
import com.kge.energy.crm.external.wechat.officialaccount.resp.GetPublishArticleResp;
import com.kge.platform.framework.web.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatOfficialAccountInfraService {

    private final WeChatOfficialAccountProperties weChatOfficialAccountProperties;

    private final WeChatCommonInfraService weChatCommonInfraService;

    /**
     * 获取稳定版接口调用凭据
     */
    public String getAccessToken() {

        return weChatCommonInfraService.getAccessToken(
                weChatOfficialAccountProperties.getAppId(), weChatOfficialAccountProperties.getAppSecret()
        );
    }

    /**
     * 获取成功发布文章列表
     * https://developers.weixin.qq.com/doc/offiaccount/Publish/Get_publication_records.html
     */
    public GetPublishArticleResp getPublishArticle(GetPublishArticleReq req) {

        String url = String.format("%s/cgi-bin/freepublish/batchget?access_token=%s",
                weChatOfficialAccountProperties.getWxUrl(), getAccessToken());

        String result = HttpUtil.post(url, JsonUtils.serialize(req));

        return JsonUtils.deserialize(result, GetPublishArticleResp.class);
    }

}
