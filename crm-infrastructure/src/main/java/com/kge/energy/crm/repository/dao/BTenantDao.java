package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.BTenant;
import com.kge.energy.crm.repository.entityext.param.TenantQueryParam;
import com.kge.energy.crm.repository.entityext.result.TenantListForOrgResult;
import com.kge.energy.crm.repository.entityext.result.TenantListResult;
import com.kge.energy.crm.repository.mapper.BTenantMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 租户表(BTenant)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class BTenantDao extends ServiceImpl<BTenantMapper, BTenant> {

    private final BTenantMapper mapper;

    public IPage<TenantListResult> selectPage(TenantQueryParam param){
        Page<TenantListResult> page = new Page<>(param.getCurrentPage(), param.getPageSize());
        return mapper.selectPage(page, param);
    }

    public List<TenantListForOrgResult> getTenantDictList(Integer tenantId) {
        return mapper.getTenantDictList(tenantId);
    }

}

