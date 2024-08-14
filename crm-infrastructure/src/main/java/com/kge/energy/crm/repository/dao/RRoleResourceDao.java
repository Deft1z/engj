package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.RRoleResource;
import com.kge.energy.crm.repository.mapper.RRoleResourceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 角色资源权限(RRoleResource)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class RRoleResourceDao extends ServiceImpl<RRoleResourceMapper, RRoleResource> {

    private final RRoleResourceMapper mapper;

    public int removeByRoleId(Integer roleId) {
        LambdaUpdateWrapper wrapper = new LambdaUpdateWrapper<>(RRoleResource.class)
                .eq(RRoleResource::getRoleId, roleId);
        return mapper.delete(wrapper);
    }

    public int removeByRoleIdWithSystemType(Integer roleId, String systemType) {
        return mapper.removeByRoleIdWithSystemType(roleId, systemType);
    }
}

