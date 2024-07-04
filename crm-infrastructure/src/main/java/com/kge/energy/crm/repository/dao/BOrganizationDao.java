package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.BOrganizationMapper;
import com.kge.energy.crm.repository.entity.BOrganization;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 机构表(BOrganization)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class BOrganizationDao extends ServiceImpl<BOrganizationMapper, BOrganization> {

    private final BOrganizationMapper mapper;

}

