package com.kge.energy.crm.news.controller;

import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.news.req.IndexAllChannelNewsReq;
import com.kge.energy.crm.news.req.PageNewsReq;
import com.kge.energy.crm.news.resp.IndexAllChannelNewsResp;
import com.kge.energy.crm.news.resp.NewsChannelResp;
import com.kge.energy.crm.news.service.NewsService;
import com.kge.energy.crm.repository.entityext.result.news.NewsDetailResult;
import com.kge.energy.crm.repository.entityext.result.news.NewsListResult;
import com.kge.platform.framework.common.net.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author wangjihua
 */
@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@Tag(name = "新闻模块API")
public class NewsController {

    private final NewsService newsService;

    @Operation(summary = "新闻渠道类型")
    @GetMapping("/channels")
    public CommonResult<NewsChannelResp> channels() {
        return CommonResult.suc(newsService.channels());
    }

    @Operation(summary = "首页所有渠道新闻列表")
    @PostMapping("/indexAllChannelNews")
    public CommonResult<IndexAllChannelNewsResp> indexAllChannelNews(@Validated @RequestBody IndexAllChannelNewsReq req) {
        return CommonResult.suc(newsService.indexAllChannelNews(req));
    }

    @Operation(summary = "渠道各类型新闻分页列表")
    @PostMapping("/pageNews")
    public CommonResult<PageResp<NewsListResult>> pageNews(@Validated @RequestBody PageNewsReq req) {
        return CommonResult.suc(newsService.pageNews(req));
    }

    @Operation(summary = "新闻详情")
    @Parameter(name = "newsId", description = "新闻ID", in = ParameterIn.PATH)
    @GetMapping("/detail/{newsId}")
    public CommonResult<NewsDetailResult> newsDetail(@Validated @PathVariable(name = "newsId") Integer newsId) {
        return CommonResult.suc(newsService.newsDetail(newsId));
    }

    @Operation(summary = "删除新闻")
    @Parameter(name = "newsId", description = "新闻ID", in = ParameterIn.PATH)
    @GetMapping("/delete/{newsId}")
    public CommonResult<Boolean> delete(@Validated @PathVariable(name = "newsId") Integer newsId) {
        return CommonResult.suc(newsService.delete(newsId));
    }


}
