package com.kge.energy.crm.repository.dao;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.BRole;
import com.kge.energy.crm.repository.entityext.param.RoleListParam;
import com.kge.energy.crm.repository.mapper.BRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色(BRole)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class BRoleDao extends ServiceImpl<BRoleMapper, BRole> {

    private final BRoleMapper mapper;

    public Page selectPage(RoleListParam param) {
        Page<BRole> page = new Page<>(param.getCurrentPage(), param.getPageSize());

        LambdaQueryWrapper<BRole> wrapper = new LambdaQueryWrapper<BRole>()
                .eq(BRole::getTenantId, param.getTenantId());

        if (CollectionUtil.isNotEmpty(param.getExcludeCodes())) {
            wrapper.notIn(BRole::getCode, param.getExcludeCodes())
                    .or()
                    .isNull(BRole::getCode);
        }

        return mapper.selectPage(page, wrapper);
    }


    public List<Integer> roleResource(Integer roleId, String systemType) {

        Assert.notNull(roleId);
        Assert.notBlank(systemType);

        return mapper.roleResource(roleId, systemType);
    }

    public List<BRole> userRole(Integer tenantId, Integer userId) {
        return mapper.userRole(tenantId, userId);
    }

    public Integer getRoleIdByCode(String code, Integer tenantId) {
        return mapper.getRoleIdByCode(code, tenantId);
    }

    public BRole getTenantRoleByCode(Integer tenantId, String code) {
        LambdaQueryWrapper<BRole> wrapper = new LambdaQueryWrapper<BRole>()
                .eq(BRole::getTenantId, tenantId)
                .eq(BRole::getCode, code);

        return mapper.selectOne(wrapper, false);
    }
}

