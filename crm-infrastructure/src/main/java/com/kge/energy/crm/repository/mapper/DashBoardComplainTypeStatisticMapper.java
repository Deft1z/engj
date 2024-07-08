package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kge.energy.crm.repository.entity.DashBoardComplainTypeStatistic;
import com.kge.energy.crm.repository.entityext.param.DashBoardParam;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DashBoardComplainTypeStatisticMapper extends BaseMapper<DashBoardComplainTypeStatistic> {
    DashBoardComplainTypeStatistic selectOne(DashBoardParam param);
}
