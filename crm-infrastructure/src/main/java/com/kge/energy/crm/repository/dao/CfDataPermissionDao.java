package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.CfDataPermission;
import com.kge.energy.crm.repository.entityext.param.DataPermissionListParam;
import com.kge.energy.crm.repository.mapper.CfDataPermissionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * 数据权限配置表(CfDataPermission)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class CfDataPermissionDao extends ServiceImpl<CfDataPermissionMapper, CfDataPermission> {

    private final CfDataPermissionMapper mapper;

    public Page<CfDataPermission> list(DataPermissionListParam param) {
        Page<CfDataPermission> page = new Page<>(param.getCurrentPage(), param.getPageSize());
        return mapper.list(page, param);
    }

    public List<CfDataPermission> findConfig(Integer bizFunctionId, Integer roleId, Integer dataRangeType) {
        LambdaQueryWrapper<CfDataPermission> wrapper = new LambdaQueryWrapper<CfDataPermission>()
                .eq(CfDataPermission::getBizFunctionId, bizFunctionId)
                .eq(CfDataPermission::getRoleId, roleId)
                .eq(CfDataPermission::getDataRangeType, dataRangeType);

        return mapper.selectList(wrapper);
    }


    public List<CfDataPermission> getDataPermission(Integer tenantId, Set<Integer> roleIds, String functionCode) {
        return mapper.getDataPermission(tenantId, roleIds, functionCode);
    }
}

