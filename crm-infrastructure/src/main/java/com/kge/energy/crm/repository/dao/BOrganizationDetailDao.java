package com.kge.energy.crm.repository.dao;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.BOrganizationDetail;
import com.kge.energy.crm.repository.entityext.result.OrgDetailResult;
import com.kge.energy.crm.repository.mapper.BOrganizationDetailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 组织详情表(BOrganizationDetail)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class BOrganizationDetailDao extends ServiceImpl<BOrganizationDetailMapper, BOrganizationDetail> {

    private final BOrganizationDetailMapper mapper;

    public List<OrgDetailResult> getOrgDetailList(Integer tenantId) {
        return mapper.getOrgDetailList(tenantId);
    }

}

