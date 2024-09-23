package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.repository.entity.CfDataPermission;
import com.kge.energy.crm.repository.entityext.param.DataPermissionListParam;
import org.apache.ibatis.annotations.Param;

/**
 * 数据权限配置表(CfDataPermission)表数据库接口层
 */
public interface CfDataPermissionMapper extends BaseMapper<CfDataPermission> {

    Page<CfDataPermission> list(Page<CfDataPermission> page, @Param("param") DataPermissionListParam param);
}

