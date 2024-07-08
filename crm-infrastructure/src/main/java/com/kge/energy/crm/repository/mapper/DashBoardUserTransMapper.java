package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kge.energy.crm.repository.entity.DashBoardUserTrans;
import com.kge.energy.crm.repository.entityext.param.DashBoardParam;

public interface DashBoardUserTransMapper extends BaseMapper<DashBoardUserTrans> {
    DashBoardUserTrans selectOne(DashBoardParam param);
}
