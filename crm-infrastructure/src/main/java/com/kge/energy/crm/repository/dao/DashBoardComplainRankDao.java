package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.DashBoardComplainRank;
import com.kge.energy.crm.repository.entityext.param.DashBoardParam;
import com.kge.energy.crm.repository.mapper.DashBoardComplainRankMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DashBoardComplainRankDao extends ServiceImpl<DashBoardComplainRankMapper, DashBoardComplainRank> {
    private final DashBoardComplainRankMapper mapper;

    public List<DashBoardComplainRank> getList(DashBoardParam param) {
        return mapper.selectList(param);
    }
}
