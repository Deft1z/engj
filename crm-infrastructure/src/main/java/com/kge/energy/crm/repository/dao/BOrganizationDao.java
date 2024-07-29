package com.kge.energy.crm.repository.dao;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.repository.entity.BOrganization;
import com.kge.energy.crm.repository.entityext.result.OrgDictResult;
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

    public List<UserInfoDto.Organization> findUserInfoDtoOrOrgs(Integer userId) {

        Assert.notNull(userId, "userId must not be null");

        return mapper.findUserInfoDtoOrOrgs(userId);
    }

    public BOrganization getOrgByUserId(Integer userId) {

        Assert.notNull(userId, "userId must not be null");

        return mapper.getOrgByUserId(userId);
    }

    public List<OrgDictResult> getOrgDictList() {
        return mapper.getOrgDictList();
    }

    public List<OrgResult> getCompanyList(){
        return mapper.getCompanyList();
    }
}

