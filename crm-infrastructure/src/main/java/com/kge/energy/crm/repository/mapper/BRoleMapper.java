package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kge.energy.crm.repository.entity.BRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色(BRole)表数据库接口层
 */
public interface BRoleMapper extends BaseMapper<BRole> {

    List<Integer> roleResource(@Param("roleId") Integer roleId, @Param("systemType") String systemType);

    List<BRole> userRole(@Param("tenantId") Integer tenantId, @Param("userId") Integer userId);

    Integer getRoleIdByCode(@Param("code") String code, @Param("tenantId") Integer tenantId);
}

