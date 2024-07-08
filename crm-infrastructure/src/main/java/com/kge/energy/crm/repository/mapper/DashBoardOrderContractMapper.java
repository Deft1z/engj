package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kge.energy.crm.repository.entity.DashBoardOrderContract;
import com.kge.energy.crm.repository.entityext.param.DashBoardParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DashBoardOrderContractMapper extends BaseMapper<DashBoardOrderContract> {
    List<DashBoardOrderContract> selectList(DashBoardParam param);
}
