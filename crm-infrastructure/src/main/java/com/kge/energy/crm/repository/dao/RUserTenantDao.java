package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.RUserTenantMapper;
import com.kge.energy.crm.repository.entity.RUserTenant;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 用户租户关系(RUserTenant)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class RUserTenantDao extends ServiceImpl<RUserTenantMapper, RUserTenant> {

    private final RUserTenantMapper mapper;

}

