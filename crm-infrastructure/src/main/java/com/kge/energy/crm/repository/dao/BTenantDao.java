package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.BTenant;
import com.kge.energy.crm.repository.mapper.BTenantMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 租户表(BTenant)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class BTenantDao extends ServiceImpl<BTenantMapper, BTenant> {

    private final BTenantMapper mapper;

}

