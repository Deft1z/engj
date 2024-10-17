package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.WComplainFlow;
import com.kge.energy.crm.repository.mapper.WComplainFlowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * w_complain_flow 投诉反馈流程(WComplainFlow)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class WComplainFlowDao extends ServiceImpl<WComplainFlowMapper, WComplainFlow> {

    private final WComplainFlowMapper mapper;

}

