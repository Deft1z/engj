package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.ROrganizationServiceType;
import com.kge.energy.crm.repository.mapper.ROrganizationServiceTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 公司服务清单(ROrganizationServiceType)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class ROrganizationServiceTypeDao extends ServiceImpl<ROrganizationServiceTypeMapper, ROrganizationServiceType> {

    private final ROrganizationServiceTypeMapper mapper;

}

