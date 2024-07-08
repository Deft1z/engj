package com.kge.energy.crm.dashboard.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.dashboard.req.DashBoardReq;
import com.kge.energy.crm.dashboard.resp.*;
import com.kge.energy.crm.dashboard.service.DashBoardService;
import com.kge.energy.crm.repository.entity.DashBoardComplainRank;
import com.kge.energy.crm.repository.entity.DashBoardComplainTypeStatistic;
import com.kge.energy.crm.repository.entityext.param.DashBoardParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DashBoardController {

    private final DashBoardService dashBoardService;

    // 查询统计上半部分
    @PostMapping("/externalBack/aggregateData/dashboardData")
    public CommonResponse<StatisticResp1> getStatistic1(@RequestBody DashBoardReq req) {
        StatisticResp1 resp = new StatisticResp1();
        BeanUtils.copyProperties(dashBoardService.getStatistic(transDateTime(req)), resp);
        return CommonResponse.suc(resp);
    }

    // 查询统计下半部分
    @PostMapping("/workMgrBack/contractBack/showOderContractNum")
    public CommonResponse<StatisticResp2> getStatistic2(@RequestBody DashBoardReq req) {
        StatisticResp2 resp = new StatisticResp2();

        HashMap<String, String> mapping = new HashMap<>();
        mapping.put("sentNum", "sendDanNum");
        mapping.put("terminateNum", "endNum");
        mapping.put("processingNum", "startNum");

        BeanUtil.copyProperties(dashBoardService.getStatistic(transDateTime(req)), resp, CopyOptions.create().setFieldMapping(mapping));
        return CommonResponse.suc(resp);
    }

    // 查询工单合同数量变化
    @PostMapping("/chart/contractBack/contractOrderChart")
    public CommonResponse<List<OrderContractResp>> getOrderContractChart(@RequestBody DashBoardReq req) {

        HashMap<String, String> mapping = new HashMap<>();
        mapping.put("date", "timeStr");

        DashBoardParam param = transDateTime(req);
        switch (req.getMode()) {
            // 按公司筛选
            case 2 -> param.setOrgId(req.getCompanyId());
            // 按区域筛选
            case 3 -> param.setArea(req.getArea());
        }

        return CommonResponse.suc(BeanUtil.copyToList(dashBoardService.getOrderContract(param), OrderContractResp.class, CopyOptions.create().setFieldMapping(mapping)));
    }


    // 查询用户转化情况
    @PostMapping("/chart/userBackMrg/UserChart")
    public CommonResponse<UserTransResp> getUserTransChart(@RequestBody DashBoardReq req) {
        UserTransResp resp = new UserTransResp();

        HashMap<String, String> mapping = new HashMap<>();
        mapping.put("activeUserNum", "newUserNum");
        mapping.put("activeUserTransRate", "newUserTransRate");

        BeanUtil.copyProperties(dashBoardService.getUserTrans(transDateTime(req)), resp, CopyOptions.create().setFieldMapping(mapping));
        return CommonResponse.suc(resp);
    }

    // 查询公司评价分布
    @PostMapping("/chart/contractBack/evaluateChart")
    public CommonResponse<EvaluateListResp> getEvaluateChart() {
        EvaluateListResp resp = new EvaluateListResp();

        HashMap<String, String> mapping = new HashMap<>();
        mapping.put("star", "start");
        mapping.put("entNum", "starNum");

        List<EvaluateResp> evaList = BeanUtil.copyToList(dashBoardService.getEvaluateList(), EvaluateResp.class, CopyOptions.create().setFieldMapping(mapping));
        resp.setList(evaList);
        resp.setAverage(dashBoardService.getEvaluateAverage());
        return CommonResponse.suc(resp);
    }

    // 查询投诉类型占比
    @PostMapping("/chart/complainBack/complainPerChart")
    public CommonResponse<DashBoardComplainTypeStatistic> getComplainPieChart(@RequestBody DashBoardReq req) {
        return CommonResponse.suc(dashBoardService.getComplainTypeStatistic(transDateTime(req)));
    }

    // 查询投诉单位排名
    @PostMapping("/chart/complainBack/complainRankChart")
    public CommonResponse<List<DashBoardComplainRank>> getComplainRank(@RequestBody DashBoardReq req) {
        return CommonResponse.suc(dashBoardService.getComplainRank(transDateTime(req)));
    }

    private DashBoardParam transDateTime(DashBoardReq req) {
        LocalDateTime startTime = LocalDateTimeUtil.parse(req.getStartTime(), "yyyy-MM");
        LocalDateTime endTime = LocalDateTimeUtil.parse(req.getEndTime(), "yyyy-MM");

        LocalDate dateTmp = endTime.toLocalDate();
        // 获取当月最后一天
        LocalDate lastDayOfMonth = dateTmp.withDayOfMonth(dateTmp.lengthOfMonth());
        // 设置时间为 23:59:59
        endTime = lastDayOfMonth.atTime(LocalTime.MAX);

        DashBoardParam param = new DashBoardParam();
        param.setStartTime(startTime);
        param.setEndTime(endTime);
        return param;
    }
}
