package com.kge.energy.crm.dashboard.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.kge.energy.crm.common.go.ConvertToGoFormats;
import com.kge.energy.crm.dashboard.req.DashBoardContractOrderReq;
import com.kge.energy.crm.dashboard.req.DashBoardReq;
import com.kge.energy.crm.dashboard.resp.*;
import com.kge.energy.crm.dashboard.service.DashBoardService;
import com.kge.energy.crm.repository.entityext.param.DashBoardParam;
import com.kge.energy.crm.repository.entityext.result.DashBoardComplainRank;
import com.kge.energy.crm.repository.entityext.result.DashBoardComplainTypeStatistic;
import com.kge.energy.crm.repository.entityext.result.StatisticalDataResult;
import com.kge.platform.framework.common.net.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
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

    /**
     * 查询公司列表
     */
    @ConvertToGoFormats
    @PostMapping("/workMgrBack/contractBack/getAllCompany")
    public CommonResult<List<CompanyResp>> getCompanyList() {
        List<CompanyResp> resp = BeanUtil.copyToList(dashBoardService.getCompanyList(), CompanyResp.class);
        return CommonResult.suc(resp);
    }

    @Operation(summary = "控制台客户、工单、合同统计")
    @PostMapping("/dashBoard/statisticalData")
    public CommonResult<StatisticalDataResult> statisticalData() {
        return CommonResult.suc(dashBoardService.statisticalData());
    }

    /**
     * 查询工单合同数量变化
     */
    @ConvertToGoFormats
    @PostMapping("/chart/contractBack/contractOrderChart")
    public CommonResult<List<OrderContractResp>> getOrderContractChart(@Validated @RequestBody DashBoardContractOrderReq req) {
        HashMap<String, String> mapping = new HashMap<>();
        mapping.put("date", "timeStr");

        DashBoardParam param = transDateTime(req);
        switch (req.getMode()) {
            // 按公司筛选
            case 2 -> param.setOrgId(req.getCompanyId());
            // 按区域筛选
            case 3 -> param.setArea(req.getArea());
        }

        return CommonResult.suc(BeanUtil.copyToList(dashBoardService.getOrderContractList(param), OrderContractResp.class, CopyOptions.create().setFieldMapping(mapping)));
    }

    /**
     * 查询用户转化情况
     */
    @ConvertToGoFormats
    @PostMapping("/chart/userBackMrg/UserChart")
    public CommonResult<UserTransResp> getUserTransChart(@Validated @RequestBody DashBoardReq req) {
        UserTransResp resp = new UserTransResp();

        HashMap<String, String> mapping = new HashMap<>();
        mapping.put("activeUserNum", "newUserNum");
        mapping.put("activeUserTransRate", "newUserTransRate");

        BeanUtil.copyProperties(dashBoardService.getUserTrans(transDateTime(req)), resp, CopyOptions.create().setFieldMapping(mapping));
        return CommonResult.suc(resp);
    }

    /**
     * 查询公司评价分布
     */
    @ConvertToGoFormats
    @PostMapping("/chart/contractBack/evaluateChart")
    public CommonResult<EvaluateListResp> getEvaluateChart() {
        EvaluateListResp resp = new EvaluateListResp();

        HashMap<String, String> mapping = new HashMap<>();
        mapping.put("star", "start");
        mapping.put("entNum", "starNum");

        List<EvaluateResp> evaList = BeanUtil.copyToList(dashBoardService.getEvaluateList(), EvaluateResp.class, CopyOptions.create().setFieldMapping(mapping));
        resp.setList(evaList);
        resp.setAverage(dashBoardService.getEvaluateAverage());
        return CommonResult.suc(resp);
    }

    /**
     * 查询投诉类型占比
     */
    @ConvertToGoFormats
    @PostMapping("/chart/complainBack/complainPerChart")
    public CommonResult<DashBoardComplainTypeStatistic> getComplainPieChart(@Validated @RequestBody DashBoardReq req) {
        return CommonResult.suc(dashBoardService.getComplainTypeStatistic(transDateTime(req)));
    }

    /**
     * 查询投诉单位排名
     */
    @ConvertToGoFormats
    @PostMapping("/chart/complainBack/complainRankChart")
    public CommonResult<List<DashBoardComplainRank>> getComplainRank(@Validated @RequestBody DashBoardReq req) {
        return CommonResult.suc(dashBoardService.getComplainRankList(transDateTime(req)));
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
