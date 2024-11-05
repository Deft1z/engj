package com.kge.energy.crm.dashboard.service;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.kge.energy.crm.repository.dao.BOrganizationDao;
import com.kge.energy.crm.repository.dao.DashBoardDao;
import com.kge.energy.crm.repository.entity.BOrganization;
import com.kge.energy.crm.repository.entityext.param.DashBoardParam;
import com.kge.energy.crm.repository.entityext.result.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashBoardService {

    private final DashBoardDao dashBoardDao;
    private final BOrganizationDao orgDao;

    public List<BOrganization> getCompanyList() {
        return orgDao.list();
    }

  
    public DashBoardUserTrans getUserTrans(DashBoardParam param) {
        return dashBoardDao.getUserTrans(param);
    }

    public List<DashBoardEvaluate> getEvaluateList() {
        return dashBoardDao.getEvaluateList();
    }

    public Double getEvaluateAverage() {
        return dashBoardDao.getEvaluateAverage();
    }

    public DashBoardComplainTypeStatistic getComplainTypeStatistic(DashBoardParam param) {
        return dashBoardDao.getComplainTypeStatistic(param);
    }

    public List<DashBoardComplainRank> getComplainRankList(DashBoardParam param) {
        return dashBoardDao.getComplainRankList(param);
    }

    public List<DashBoardOrderContract> getOrderContractList(DashBoardParam param) {
        Date startDate = DateUtil.date(param.getStartTime());
        Date endDate = DateUtil.date(param.getEndTime());

        // 生成日期集合
        List<DateTime> tmpList = DateUtil.rangeToList(startDate, endDate, DateField.MONTH);
        // 转String类型集合作为sql参数
        param.setDateList(tmpList.stream().map(date -> DateUtil.format(date, "yyyy-MM")).collect(Collectors.toList()));
        return dashBoardDao.getOrderContractList(param);
    }

}
