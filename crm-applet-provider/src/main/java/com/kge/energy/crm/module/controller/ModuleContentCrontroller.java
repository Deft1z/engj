package com.kge.energy.crm.module.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.external.wechat.officialaccount.req.GetPublishArticleReq;
import com.kge.energy.crm.external.wechat.officialaccount.resp.GetPublishArticleResp;
import com.kge.energy.crm.module.service.ModuleContentService;
import com.kge.platform.framework.common.net.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangjihua
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
public class ModuleContentCrontroller {

    private final ModuleContentService moduleContentService;


    @Operation(summary = "获取南投集团公众号推文新闻")
    @PostMapping("/contentMgr/moduleContentMgr/getNews")
    @ConvertToGoFormats
    public CommonResult<GetPublishArticleResp> getNews(@Validated @RequestBody GetPublishArticleReq req) {
        return CommonResult.suc(moduleContentService.getNews(req));
    }

}
