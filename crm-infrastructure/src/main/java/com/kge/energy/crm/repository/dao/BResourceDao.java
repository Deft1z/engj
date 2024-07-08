package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.entityext.result.ResourcePermissionResult;
import com.kge.energy.crm.repository.mapper.BResourceMapper;
import com.kge.energy.crm.repository.entity.BResource;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

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
}

