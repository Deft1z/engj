package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kge.energy.crm.repository.entity.BResource;
import com.kge.energy.crm.repository.entityext.param.SystemResourceParam;
import com.kge.energy.crm.repository.entityext.param.UserResourceParam;

import java.util.List;

/**
 * 资源表(BResource)表数据库接口层
 */
public interface BResourceMapper extends BaseMapper<BResource> {

    List<BResource> getSystemResources(SystemResourceParam param);

    List<BResource> getUserResources(UserResourceParam param);
}

