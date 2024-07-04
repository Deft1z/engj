package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.WfFormFlowMapper;
import com.kge.energy.crm.repository.entity.WfFormFlow;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 表单流程(WfFormFlow)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class WfFormFlowDao extends ServiceImpl<WfFormFlowMapper, WfFormFlow> {

    private final WfFormFlowMapper mapper;

}

