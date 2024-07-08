package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.DashBoardOrderContract;
import com.kge.energy.crm.repository.entityext.param.DashBoardParam;
import com.kge.energy.crm.repository.mapper.DashBoardOrderContractMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DashBoardOrderContractDao extends ServiceImpl<DashBoardOrderContractMapper, DashBoardOrderContract> {

    private final DashBoardOrderContractMapper mapper;

    public List<DashBoardOrderContract> getList(DashBoardParam param) {
        return mapper.selectList(param);
    }
}
