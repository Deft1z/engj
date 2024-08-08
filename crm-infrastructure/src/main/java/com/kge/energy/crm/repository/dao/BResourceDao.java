package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.BResource;
import com.kge.energy.crm.repository.entityext.param.SystemResourceParam;
import com.kge.energy.crm.repository.entityext.param.UserResourceParam;
import com.kge.energy.crm.repository.entityext.result.ResourcePermissionResult;
import com.kge.energy.crm.repository.mapper.BResourceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 资源表(BResource)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class BResourceDao extends ServiceImpl<BResourceMapper, BResource> {

    private final BResourceMapper mapper;

    public List<ResourcePermissionResult> findPermission(Integer userId, List<String> urls) {
        return mapper.findPermission(userId, urls);
    }

    public List<ResourcePermissionResult> findMenu(Integer userId) {
        return mapper.findMenu(userId);
    }

    public List<BResource> getSystemResources(SystemResourceParam param) {
        return mapper.getSystemResources(param);
    }

    public List<BResource> getUserResources(UserResourceParam param) {
        return mapper.getUserResources(param);
    }
}

