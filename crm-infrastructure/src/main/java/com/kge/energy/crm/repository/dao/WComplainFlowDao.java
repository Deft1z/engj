package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.WComplainFlowMapper;
import com.kge.energy.crm.repository.entity.WComplainFlow;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * w_complain_flow 投诉反馈流程(WComplainFlow)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class WComplainFlowDao extends ServiceImpl<WComplainFlowMapper, WComplainFlow> {

    private final WComplainFlowMapper mapper;

}

