package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kge.energy.crm.repository.entity.BResource;
import com.kge.energy.crm.repository.entityext.param.SystemResourceParam;
import com.kge.energy.crm.repository.entityext.param.UserResourceParam;
import com.kge.energy.crm.repository.entityext.result.ResourcePermissionResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资源表(BResource)表数据库接口层
 */
public interface BResourceMapper extends BaseMapper<BResource> {

    List<ResourcePermissionResult> findPermission(@Param("userId") Integer userId, @Param("urls") List<String> urls);

    List<ResourcePermissionResult> findMenu(@Param("userId") Integer userId);

    List<BResource> getSystemResources(SystemResourceParam param);

    List<BResource> getUserResources(UserResourceParam param);
}

