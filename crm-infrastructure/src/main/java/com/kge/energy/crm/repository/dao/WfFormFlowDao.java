package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.WfFormFlow;
import com.kge.energy.crm.repository.mapper.WfFormFlowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 表单流程(WfFormFlow)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class WfFormFlowDao extends ServiceImpl<WfFormFlowMapper, WfFormFlow> {

    private final WfFormFlowMapper mapper;

    public List<WfFormFlow> selectFlowByFormIdAndActionType(Integer formId, String typef) {
        LambdaQueryWrapper<WfFormFlow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WfFormFlow::getFormId, formId);
        wrapper.eq(WfFormFlow::getActionType, typef);
        return mapper.selectList(wrapper);
    }

    public WfFormFlow getLatestFormFlow(Integer formId, Integer tenantId) {
        LambdaQueryWrapper<WfFormFlow> queryWrapper = Wrappers.<WfFormFlow>lambdaQuery()
                .eq(WfFormFlow::getFormId, formId)
                .eq(WfFormFlow::getTenantId, tenantId)
                .orderByDesc(WfFormFlow::getCreateTime);
        return mapper.selectList(queryWrapper).get(0);
    }
}

