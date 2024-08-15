package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.RUserRole;
import com.kge.energy.crm.repository.mapper.RUserRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 用户角色关系(RUserRole)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class RUserRoleDao extends ServiceImpl<RUserRoleMapper, RUserRole> {

    private final RUserRoleMapper mapper;

    public int removeByUserId(Integer userId) {
        LambdaUpdateWrapper wrapper = new LambdaUpdateWrapper<>(RUserRole.class)
                .eq(RUserRole::getUserId, userId);
        return mapper.delete(wrapper);
    }
}

