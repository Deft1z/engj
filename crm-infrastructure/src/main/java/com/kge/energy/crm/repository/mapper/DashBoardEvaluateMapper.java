package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kge.energy.crm.repository.entity.DashBoardEvaluate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DashBoardEvaluateMapper extends BaseMapper<DashBoardEvaluate> {
    List<DashBoardEvaluate> selectList();

    double selectAverage();
}
