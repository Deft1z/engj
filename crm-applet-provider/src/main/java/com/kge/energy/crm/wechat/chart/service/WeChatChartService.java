package com.kge.energy.crm.wechat.chart.service;

import cn.hutool.core.date.DateUtil;
import com.kge.energy.crm.repository.dao.*;
import com.kge.energy.crm.repository.entityext.result.StartEva;
import com.kge.energy.crm.wechat.chart.req.WeChatChartReq;
import com.kge.energy.crm.wechat.chart.resp.StartEvaResp;
import com.kge.energy.crm.wechat.chart.resp.WeChatChartResp;
import com.kge.energy.crm.wechat.chart.resp.WeChatEvaluateResp;
import com.kge.energy.crm.wechat.chart.resp.WeChatStatisticsResp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WeChatChartService {

    private final BUserDao userDao;
    private final WfFormDao formDao;
    private final ScServiceContractDao serviceContractDao;
    private final ScContractEvaluateDao contractEvaluateDao;
    private final WComplainDao complaintDao;

    public WeChatChartResp getUserChart(WeChatChartReq req){
        String startTime = DateUtil.beginOfMonth(DateUtil.parse(req.getStartTime(), "yyyy-MM")).toString();
        String endTime = DateUtil.endOfMonth(DateUtil.parse(req.getEndTime(), "yyyy-MM")).toString();

        Integer newUserNum = userDao.findNewUserNum(startTime, endTime).intValue();
        Integer orderNum = formDao.findOrderNum(startTime, endTime).intValue();
        Integer contractNum = serviceContractDao.findContractNum(startTime, endTime).intValue();
        String newUserTransRate = "0%";
        String orderTransRate = "0%";

        if(newUserNum > 0){
            newUserTransRate = String.format("%.2f", 100.0 * orderNum / newUserNum) + "%";
        }

        if(orderNum > 0){
            orderTransRate = String.format("%.2f", 100.0 * contractNum / orderNum) + "%";
        }

        WeChatChartResp resp = new WeChatChartResp(newUserNum, orderNum, contractNum, newUserTransRate, orderTransRate);
        return resp;
    }

    public WeChatEvaluateResp findEvaluateNum(){
        WeChatEvaluateResp weChatEvaluateResp = new WeChatEvaluateResp();
        List<StartEvaResp> evaList = new ArrayList<>();
        for(int i = 1; i <= 5; i++) {
            StartEva startEva = contractEvaluateDao.getStartEva(i);
            StartEvaResp resp = new StartEvaResp();
            resp.setStart(i);
            resp.setStarNum(startEva.getStarNum());
            resp.setRate(Math.round(startEva.getRate()));
            evaList.add(resp);
        }

        weChatEvaluateResp.setList(evaList);
        weChatEvaluateResp.setAverage(contractEvaluateDao.getAverage());
        return weChatEvaluateResp;
    }

    public WeChatStatisticsResp getStatistics(WeChatChartReq req){
        String startTime = DateUtil.beginOfMonth(DateUtil.parse(req.getStartTime(), "yyyy-MM")).toString();
        String endTime = DateUtil.endOfMonth(DateUtil.parse(req.getEndTime(), "yyyy-MM")).toString();
        return new WeChatStatisticsResp(
                userDao.findNewUserCount(startTime, endTime).intValue(),
                formDao.findNewConsultingCount(startTime, endTime).intValue(),
                complaintDao.findComplainCount(startTime, endTime).intValue(),
                serviceContractDao.findNewContractCount(startTime, endTime).intValue()
        );
    }
}
