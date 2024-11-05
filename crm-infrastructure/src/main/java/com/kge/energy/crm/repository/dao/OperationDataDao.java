package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.entityext.param.StatisticalDataParam;
import com.kge.energy.crm.repository.entityext.result.StatisticalDataResult;
import com.kge.energy.crm.repository.mapper.OperationDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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

}
