package com.kge.energy.crm.repository.dao;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.repository.entity.BOrganization;
import com.kge.energy.crm.repository.entityext.param.OrgQueryParam;
import com.kge.energy.crm.repository.entityext.result.OrgDictResult;
import com.kge.energy.crm.repository.entityext.result.OrgListResult;
import com.kge.energy.crm.repository.entityext.result.OrgResult;
import com.kge.energy.crm.repository.mapper.BOrganizationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 机构表(BOrganization)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class BOrganizationDao extends ServiceImpl<BOrganizationMapper, BOrganization> {

    private final BOrganizationMapper mapper;

    public List<UserInfoDto.Organization> findUserInfoDtoOrgs(Integer userId) {

        Assert.notNull(userId, "userId must not be null");

        return mapper.findUserInfoDtoOrgs(userId);
    }

    public BOrganization getOrgByUserId(Integer userId) {

        Assert.notNull(userId, "userId must not be null");

        return mapper.getOrgByUserId(userId);
    }

    public List<OrgDictResult> getOrgDictList() {
        return mapper.getOrgDictList();
    }

    public List<OrgResult> getCompanyList() {
        return mapper.getCompanyList();
    }

    public List<OrgListResult> getOrgList(OrgQueryParam param) {
        return mapper.getOrgList(param);
    }

    public Integer getTopLevel(Integer tenantId) {
        return mapper.getTopLevel(tenantId);
    }

    public Long getNextLevelOrgCount(Integer orgId) {
        LambdaQueryWrapper<BOrganization> wrapper = Wrappers.<BOrganization>lambdaQuery()
                .eq(BOrganization::getParentOrganizationId, orgId);
        return mapper.selectCount(wrapper);
    }
}

