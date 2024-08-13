package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.repository.entity.BOrganization;
import com.kge.energy.crm.repository.entityext.param.OrgQueryParam;
import com.kge.energy.crm.repository.entityext.result.OrgDictResult;
import com.kge.energy.crm.repository.entityext.result.OrgListResult;
import com.kge.energy.crm.repository.entityext.result.OrgResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 机构表(BOrganization)表数据库接口层
 */
public interface BOrganizationMapper extends BaseMapper<BOrganization> {

    List<UserInfoDto.Organization> findUserInfoDtoOrgs(Integer userId);

    BOrganization getOrgByUserId(Integer userId);

    List<OrgDictResult> getOrgDictList();

    List<OrgResult> getCompanyList();

    IPage<OrgListResult> selectPage(Page<OrgListResult> page, @Param("param") OrgQueryParam param);

    void logicDelete(Integer orgId);

    Integer getTopLevel(Integer tenantId);
}

