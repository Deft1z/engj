package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.repository.entity.BTenant;
import com.kge.energy.crm.repository.entityext.param.TenantQueryParam;
import com.kge.energy.crm.repository.entityext.param.UserAlarmMsgParam;
import com.kge.energy.crm.repository.entityext.result.TenantListResult;
import org.apache.ibatis.annotations.Param;

/**
 * 租户表(BTenant)表数据库接口层
 */
public interface BTenantMapper extends BaseMapper<BTenant> {

    IPage<TenantListResult> selectPage(Page<TenantListResult> page, @Param("param") TenantQueryParam param);

    void logicDelete(Integer tenantId);

}

