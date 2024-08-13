package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.RUserTenant;
import com.kge.energy.crm.repository.mapper.RUserTenantMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 用户租户关系(RUserTenant)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class RUserTenantDao extends ServiceImpl<RUserTenantMapper, RUserTenant> {

    private final RUserTenantMapper mapper;

    public RUserTenant findTenantByUid(Integer userId) {

        LambdaQueryWrapper<RUserTenant> wrapper = Wrappers.<RUserTenant>lambdaQuery()
                .eq(RUserTenant::getUserId, userId);

        return mapper.selectOne(wrapper);
    }

    public int removeByUserId(Integer userId) {
        LambdaUpdateWrapper wrapper = new LambdaUpdateWrapper<>(RUserTenant.class)
                .eq(RUserTenant::getUserId, userId);
        return mapper.delete(wrapper);
    }

}

