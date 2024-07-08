package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.DashBoardStatistic;
import com.kge.energy.crm.repository.entityext.param.DashBoardParam;
import com.kge.energy.crm.repository.mapper.DashBoardStatisticMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DashBoardStatisticDao extends ServiceImpl<DashBoardStatisticMapper, DashBoardStatistic> {

    private final DashBoardStatisticMapper mapper;

    public DashBoardStatistic get(DashBoardParam param){
        return mapper.selectOne(param);
    }
}
