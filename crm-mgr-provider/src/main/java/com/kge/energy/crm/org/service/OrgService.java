package com.kge.energy.crm.org.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ObjectUtil;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.org.req.AddOrgReq;
import com.kge.energy.crm.org.req.DeleteOrgReq;
import com.kge.energy.crm.org.req.OrgQueryReq;
import com.kge.energy.crm.org.req.UpdateOrgReq;
import com.kge.energy.crm.org.resp.OrgDictResp;
import com.kge.energy.crm.repository.dao.BOrganizationDao;
import com.kge.energy.crm.repository.entity.BOrganization;
import com.kge.energy.crm.repository.entityext.param.OrgQueryParam;
import com.kge.energy.crm.repository.entityext.result.OrgDictResult;
import com.kge.energy.crm.repository.entityext.result.OrgListResult;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrgService {

    private final BOrganizationDao bOrganizationDao;

    public List<OrgDictResp> getOrgDictList() {

        List<OrgDictResult> orgDictResults = bOrganizationDao.getOrgDictList();

        return BeanUtil.copyToList(orgDictResults, OrgDictResp.class);
    }

    public PageResp<OrgListResult> selectPage(OrgQueryReq req){
        OrgQueryParam param = BeanUtil.copyProperties(req, OrgQueryParam.class);

        boolean isSuperAdmin = UserInfoContextUtils.isSuperAdmin();
        boolean isTenantAdmin = UserInfoContextUtils.isTenantAdmin();

        return new PageResp<>(bOrganizationDao.selectPage(param));
    }

    public Boolean add(AddOrgReq addOrgReq){
        BOrganization parentOrganization = bOrganizationDao.getById(addOrgReq.getParentOrganizationId());
        if(ObjectUtil.isNull(parentOrganization)){
            throw new ServiceException("上级组织不存在");
        }

        BOrganization organization = BeanUtil.copyProperties(addOrgReq, BOrganization.class);
        organization.setLevel(Opt.ofNullable(parentOrganization.getLevel()).orElse(0) + 1);
        organization.setFlag(1);
        return bOrganizationDao.save(organization);
    }

    public Boolean update(UpdateOrgReq updateOrgReq) {
        BOrganization old = bOrganizationDao.getById(updateOrgReq.getOrganizationId());
        if(ObjectUtil.isNull(old)){
            throw new ServiceException("组织结构不存在");
        }

        BeanUtil.copyProperties(updateOrgReq, old);
        return bOrganizationDao.saveOrUpdate(old);
    }

    public Boolean delete(DeleteOrgReq deleteOrgReq){
        BOrganization old = bOrganizationDao.getById(deleteOrgReq.getOrganizationId());
        if(ObjectUtil.isNull(old)){
            throw new ServiceException("组织结构不存在");
        }

        bOrganizationDao.logicDelete(deleteOrgReq.getOrganizationId());
        return true;
    }
}
