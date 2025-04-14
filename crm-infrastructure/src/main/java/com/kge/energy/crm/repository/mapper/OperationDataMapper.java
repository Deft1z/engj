package com.kge.energy.crm.repository.mapper;

import com.kge.energy.crm.repository.entityext.param.StatisticalDataParam;
import com.kge.energy.crm.repository.entityext.result.DashboardStatResult;
import com.kge.energy.crm.repository.entityext.result.PromoteUserDataResult;
import com.kge.energy.crm.repository.entityext.result.StatisticalDataResult;

import java.util.List;

/**
 * @author wangjihua
 */
public interface OperationDataMapper {

    StatisticalDataResult.User getUserStatistic(StatisticalDataParam param);

    StatisticalDataResult.Consulting getConsultingStatistic(StatisticalDataParam param);

    StatisticalDataResult.Contract getContractStatistic(StatisticalDataParam param);

    StatisticalDataResult.Complain getComplainStatistic(StatisticalDataParam param);

    DashboardStatResult getNewUserGrowthStat(StatisticalDataParam param);

    DashboardStatResult getOrderContractQtyStat(StatisticalDataParam param);

    DashboardStatResult getOrderContractAmountStat(StatisticalDataParam param);

    DashboardStatResult getComplainPctStat(StatisticalDataParam param);

    DashboardStatResult getComplainQtyStat(StatisticalDataParam param);

    List<PromoteUserDataResult> getPromoteUserData(StatisticalDataParam param);
}
