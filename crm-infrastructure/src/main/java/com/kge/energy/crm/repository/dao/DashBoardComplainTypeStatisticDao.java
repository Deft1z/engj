package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.DashBoardComplainTypeStatistic;
import com.kge.energy.crm.repository.entityext.param.DashBoardParam;
import com.kge.energy.crm.repository.mapper.DashBoardComplainTypeStatisticMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DashBoardComplainTypeStatisticDao extends ServiceImpl<DashBoardComplainTypeStatisticMapper, DashBoardComplainTypeStatistic> {
    private final DashBoardComplainTypeStatisticMapper mapper;

    public DashBoardComplainTypeStatistic get(DashBoardParam param) {
        return mapper.selectOne(param);
    }
}
