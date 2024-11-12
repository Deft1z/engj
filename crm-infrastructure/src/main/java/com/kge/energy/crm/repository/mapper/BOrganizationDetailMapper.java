package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kge.energy.crm.repository.entity.BOrganizationDetail;
import com.kge.energy.crm.repository.entityext.result.OrgDetailResult;

import java.util.List;


/**
 * 组织详情表(BOrganizationDetail)表数据库接口层
 */
public interface BOrganizationDetailMapper extends BaseMapper<BOrganizationDetail> {
    List<OrgDetailResult> getOrgDetailList(Integer tenantId);
}

