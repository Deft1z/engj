package com.kge.energy.crm.dashboard.service;

import com.kge.energy.crm.repository.entity.*;
import com.kge.energy.crm.repository.entityext.param.DashBoardParam;

import java.util.List;

public interface DashBoardService {
    DashBoardStatistic getStatistic(DashBoardParam param);

    DashBoardUserTrans getUserTrans(DashBoardParam param);

    List<DashBoardEvaluate> getEvaluateList();

    double getEvaluateAverage();

    DashBoardComplainTypeStatistic getComplainTypeStatistic(DashBoardParam param);

    List<DashBoardComplainRank> getComplainRank(DashBoardParam param);

    List<DashBoardOrderContract> getOrderContract(DashBoardParam param);
}
