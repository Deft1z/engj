package com.kge.energy.crm.news.controller;

import com.kge.energy.crm.news.req.UploadNewsReq;
import com.kge.energy.crm.news.service.NewsService;
import com.kge.platform.framework.common.net.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/news")
@RequiredArgsConstructor
@Tag(name = "新闻模块API")
public class NewsController {

    private final NewsService newsService;

    @Operation(summary = "上传新闻")
    @PostMapping("/uploadNews")
    public CommonResult<Boolean> uploadNews(@Validated @RequestBody UploadNewsReq req) {
        return CommonResult.suc(newsService.uploadNews(req));
    }

}
