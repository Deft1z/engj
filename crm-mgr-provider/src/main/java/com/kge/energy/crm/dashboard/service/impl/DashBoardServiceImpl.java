package com.kge.energy.crm.dashboard.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.kge.energy.crm.dashboard.service.DashBoardService;
import com.kge.energy.crm.repository.dao.*;
import com.kge.energy.crm.repository.entity.*;
import com.kge.energy.crm.repository.entityext.param.DashBoardParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashBoardServiceImpl implements DashBoardService {

    private final DashBoardStatisticDao dashBoardStatisticDao;
    private final DashBoardUserTransDao dashBoardUserTransDao;
    private final DashBoardEvaluateDao dashBoardEvaluateDao;
    private final DashBoardComplainTypeStatisticDao dashBoardComplainTypeStatisticDao;
    private final DashBoardComplainRankDao dashBoardComplainRankDao;
    private final DashBoardOrderContractDao dashBoardOrderContractDao;

    @Override
    public DashBoardStatistic getStatistic(DashBoardParam param) {
        return dashBoardStatisticDao.get(param);
    }

    @Override
    public DashBoardUserTrans getUserTrans(DashBoardParam param) {
        return dashBoardUserTransDao.get(param);
    }

    @Override
    public List<DashBoardEvaluate> getEvaluateList() {
        return dashBoardEvaluateDao.getList();
    }

    @Override
    public double getEvaluateAverage() {
        return dashBoardEvaluateDao.getAverage();
    }

    @Override
    public DashBoardComplainTypeStatistic getComplainTypeStatistic(DashBoardParam param) {
        return dashBoardComplainTypeStatisticDao.get(param);
    }

    @Override
    public List<DashBoardComplainRank> getComplainRank(DashBoardParam param) {
        return dashBoardComplainRankDao.getList(param);
    }

    @Override
    public List<DashBoardOrderContract> getOrderContract(DashBoardParam param) {
        Date startDate = DateUtil.date(param.getStartTime());
        Date endDate = DateUtil.date(param.getEndTime());

        // 生成日期集合
        List<DateTime> tmpList = DateUtil.rangeToList(startDate, endDate, DateField.MONTH);
        // 转String类型集合作为sql参数
        param.setDateList(tmpList.stream().map(date -> DateUtil.format(date, "yyyy-MM")).collect(Collectors.toList()));
        return dashBoardOrderContractDao.getList(param);
    }
}
