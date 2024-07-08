package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.DashBoardEvaluate;
import com.kge.energy.crm.repository.mapper.DashBoardEvaluateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DashBoardEvaluateDao extends ServiceImpl<DashBoardEvaluateMapper, DashBoardEvaluate> {
    private final DashBoardEvaluateMapper mapper;

    public List<DashBoardEvaluate> getList() {
        return mapper.selectList();
    }

    public double getAverage() {
        return mapper.selectAverage();
    }
}
