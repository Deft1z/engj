package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.CfBizFunction;
import com.kge.energy.crm.repository.entityext.param.BizFunctionListParam;
import com.kge.energy.crm.repository.mapper.CfBizFunctionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 业务功能配置表(CfBizFunction)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class CfBizFunctionDao extends ServiceImpl<CfBizFunctionMapper, CfBizFunction> {

    private final CfBizFunctionMapper mapper;

    public Page<CfBizFunction> list(BizFunctionListParam param) {

        Page<CfBizFunction> page = new Page<>(param.getCurrentPage(), param.getPageSize());

        return mapper.list(page, param);
    }
}

