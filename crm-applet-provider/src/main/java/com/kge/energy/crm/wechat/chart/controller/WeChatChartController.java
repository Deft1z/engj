package com.kge.energy.crm.wechat.chart.controller;

import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.wechat.chart.req.WeChatChartReq;
import com.kge.energy.crm.wechat.chart.resp.WeChatChartResp;
import com.kge.energy.crm.wechat.chart.resp.WeChatEvaluateResp;
import com.kge.energy.crm.wechat.chart.resp.WeChatStatisticsResp;
import com.kge.energy.crm.wechat.chart.service.WeChatChartService;
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
    public CommonResponse<WeChatChartResp> getUserChart(@Valid @RequestBody WeChatChartReq req) {
        return CommonResponse.suc(weChatChartService.getUserChart(req));
    }

    @PostMapping("/evaluateChart")
    @ConvertToGoFormats
    public CommonResponse<WeChatEvaluateResp> findEvaluateNum() {
        return CommonResponse.suc(weChatChartService.findEvaluateNum());
    }

    @PostMapping("/statistics")
    @ConvertToGoFormats
    public CommonResponse<WeChatStatisticsResp> getStatistics(@Valid @RequestBody WeChatChartReq req) {
        return CommonResponse.suc(weChatChartService.getStatistics(req));
    }
}
