package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kge.energy.crm.repository.entity.RRoleResource;
import org.apache.ibatis.annotations.Param;

/**
 * 角色资源权限(RRoleResource)表数据库接口层
 */
public interface RRoleResourceMapper extends BaseMapper<RRoleResource> {

    int removeByRoleIdWithSystemType(@Param("roleId") Integer roleId, @Param("systemType") String systemType);
}

