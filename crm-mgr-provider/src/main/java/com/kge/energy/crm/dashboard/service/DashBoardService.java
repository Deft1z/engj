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

    private final DashBoardDao dao;
    private final BOrganizationDao orgDao;

    public List<BOrganization> getCompanyList() {
        return orgDao.list();
    }

    public DashBoardStatistic getStatistic(DashBoardParam param) {
        return dao.getStatistic(param);
    }

    public DashBoardUserTrans getUserTrans(DashBoardParam param) {
        return dao.getUserTrans(param);
    }

    public List<DashBoardEvaluate> getEvaluateList() {
        return dao.getEvaluateList();
    }

    public Double getEvaluateAverage() {
        return dao.getEvaluateAverage();
    }

    public DashBoardComplainTypeStatistic getComplainTypeStatistic(DashBoardParam param) {
        return dao.getComplainTypeStatistic(param);
    }

    public List<DashBoardComplainRank> getComplainRankList(DashBoardParam param) {
        return dao.getComplainRankList(param);
    }

    public List<DashBoardOrderContract> getOrderContractList(DashBoardParam param) {
        Date startDate = DateUtil.date(param.getStartTime());
        Date endDate = DateUtil.date(param.getEndTime());

        // 生成日期集合
        List<DateTime> tmpList = DateUtil.rangeToList(startDate, endDate, DateField.MONTH);
        // 转String类型集合作为sql参数
        param.setDateList(tmpList.stream().map(date -> DateUtil.format(date, "yyyy-MM")).collect(Collectors.toList()));
        return dao.getOrderContractList(param);
    }
}
