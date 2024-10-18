package com.kge.energy.crm.wechat.chart.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.wechat.chart.req.WeChatChartReq;
import com.kge.energy.crm.wechat.chart.resp.WeChatChartResp;
import com.kge.energy.crm.wechat.chart.resp.WeChatEvaluateResp;
import com.kge.energy.crm.wechat.chart.resp.WeChatStatisticsResp;
import com.kge.energy.crm.wechat.chart.service.WeChatChartService;
import com.kge.platform.framework.common.net.CommonResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wechat/chart")
@RequiredArgsConstructor
public class WeChatChartController {
    private final WeChatChartService weChatChartService;

    @PostMapping("/UserChart")
    @ConvertToGoFormats
    public CommonResult<WeChatChartResp> getUserChart(@Valid @RequestBody WeChatChartReq req) {
        return CommonResult.suc(weChatChartService.getUserChart(req));
    }

    @PostMapping("/evaluateChart")
    @ConvertToGoFormats
    public CommonResult<WeChatEvaluateResp> findEvaluateNum() {
        return CommonResult.suc(weChatChartService.findEvaluateNum());
    }

    @PostMapping("/statistics")
    @ConvertToGoFormats
    public CommonResult<WeChatStatisticsResp> getStatistics(@Valid @RequestBody WeChatChartReq req) {
        return CommonResult.suc(weChatChartService.getStatistics(req));
    }
}
