package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.entityext.param.DashBoardParam;
import com.kge.energy.crm.repository.entityext.result.*;
import com.kge.energy.crm.repository.mapper.DashBoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DashBoardDao {
    private final DashBoardMapper mapper;

    public List<DashBoardOrderContract> getOrderContractList(DashBoardParam param) {
        return mapper.selectOrderContractList(param);
    }

    public DashBoardUserTrans getUserTrans(DashBoardParam param) {
        return mapper.selectUserTrans(param);
    }

    public List<DashBoardEvaluate> getEvaluateList() {
        return mapper.selectEvaluateList();
    }

    public Double getEvaluateAverage() {
        return mapper.selectEvaluateAverage();
    }

    public DashBoardComplainTypeStatistic getComplainTypeStatistic(DashBoardParam param) {
        return mapper.selectComplainTypeStatistic(param);
    }

    public List<DashBoardComplainRank> getComplainRankList(DashBoardParam param) {
        return mapper.selectComplainRankList(param);
    }

    public StatisticalDataResult.User getUserStatistic(Integer tenantId) {
        return mapper.getUserStatistic(tenantId);
    }

    public StatisticalDataResult.Consulting getConsultingStatistic(Integer tenantId) {
        return mapper.getConsultingStatistic(tenantId);
    }

    public StatisticalDataResult.Contract getContractStatistic(Integer tenantId) {
        return mapper.getContractStatistic(tenantId);
    }
}
