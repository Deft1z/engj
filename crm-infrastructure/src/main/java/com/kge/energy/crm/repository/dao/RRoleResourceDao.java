package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.RRoleResourceMapper;
import com.kge.energy.crm.repository.entity.RRoleResource;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 角色资源权限(RRoleResource)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class RRoleResourceDao extends ServiceImpl<RRoleResourceMapper, RRoleResource> {

    private final RRoleResourceMapper mapper;

}

