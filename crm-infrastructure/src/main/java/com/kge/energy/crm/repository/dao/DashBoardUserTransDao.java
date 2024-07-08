package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.DashBoardUserTrans;
import com.kge.energy.crm.repository.entityext.param.DashBoardParam;
import com.kge.energy.crm.repository.mapper.DashBoardUserTransMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DashBoardUserTransDao extends ServiceImpl<DashBoardUserTransMapper, DashBoardUserTrans> {

    private final DashBoardUserTransMapper mapper;

    public DashBoardUserTrans get(DashBoardParam param){
        return mapper.selectOne(param);
    }
}
