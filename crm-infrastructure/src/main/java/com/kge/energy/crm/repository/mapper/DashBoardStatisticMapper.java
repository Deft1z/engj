package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kge.energy.crm.repository.entity.DashBoardStatistic;
import com.kge.energy.crm.repository.entityext.param.DashBoardParam;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DashBoardStatisticMapper extends BaseMapper<DashBoardStatistic> {
    DashBoardStatistic selectOne(DashBoardParam param);
}
