package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.entityext.param.StatisticalDataParam;
import com.kge.energy.crm.repository.entityext.result.DashboardStatResult;
import com.kge.energy.crm.repository.entityext.result.PromoteUserDataResult;
import com.kge.energy.crm.repository.entityext.result.StatisticalDataResult;
import com.kge.energy.crm.repository.mapper.OperationDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wangjihua
 */

@Repository
@RequiredArgsConstructor
public class OperationDataDao {

    private final OperationDataMapper mapper;

    public StatisticalDataResult.User getUserStatistic(StatisticalDataParam param) {
        return mapper.getUserStatistic(param);
    }

    public StatisticalDataResult.Consulting getConsultingStatistic(StatisticalDataParam param) {
        return mapper.getConsultingStatistic(param);
    }

    public StatisticalDataResult.Contract getContractStatistic(StatisticalDataParam param) {
        return mapper.getContractStatistic(param);
    }

    public StatisticalDataResult.Complain getComplainStatistic(StatisticalDataParam param) {
        return mapper.getComplainStatistic(param);
    }

    public DashboardStatResult getNewUserGrowthStat(StatisticalDataParam param) {
        return mapper.getNewUserGrowthStat(param);
    }

    public DashboardStatResult getOrderContractQtyStat(StatisticalDataParam param) {
        return mapper.getOrderContractQtyStat(param);
    }

    public DashboardStatResult getOrderContractAmountStat(StatisticalDataParam param) {
        return mapper.getOrderContractAmountStat(param);
    }

    public DashboardStatResult getComplainPctStat(StatisticalDataParam param) {
        return mapper.getComplainPctStat(param);
    }

    public DashboardStatResult getComplainQtyStat(StatisticalDataParam param) {
        return mapper.getComplainQtyStat(param);
    }

    public List<PromoteUserDataResult> getPromoteUserData(StatisticalDataParam param) {
        return mapper.getPromoteUserData(param);
    }


}
