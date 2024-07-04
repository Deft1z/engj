package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.ROrganizationServiceTypeMapper;
import com.kge.energy.crm.repository.entity.ROrganizationServiceType;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 公司服务清单(ROrganizationServiceType)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class ROrganizationServiceTypeDao extends ServiceImpl<ROrganizationServiceTypeMapper, ROrganizationServiceType> {

    private final ROrganizationServiceTypeMapper mapper;

}

