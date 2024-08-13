package com.kge.energy.crm.org.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.AuthVerifyUtils;
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

    public List<OrgListResult> selectList(OrgQueryReq req){
        AuthVerifyUtils.mustAdmin();

        OrgQueryParam param = BeanUtil.copyProperties(req, OrgQueryParam.class);

        //如果查询条件为空
        if(ObjectUtil.isEmpty(req.getName()) && ObjectUtil.isNull(req.getTenantId())){
            //如果不查下级，返回该账号能看到的最高级
            if(ObjectUtil.isNull(req.getParentOrganizationId())){
                Integer topLevel = bOrganizationDao.getTopLevel(UserInfoContextUtils.getCurrentTenantId());
                Assert.notNull(topLevel, "用户组织最高层级不存在");
                param.setLevel(topLevel);
            }
        }

        //如果是租户管理员，只能看到自己租户的组织
        boolean isTenantAdmin = AuthVerifyUtils.isTenantAdmin();
        if(isTenantAdmin){
            param.setTenantId(UserInfoContextUtils.getCurrentTenantId());
        }

        return bOrganizationDao.selectList(param);
    }

    public Boolean add(AddOrgReq addOrgReq){
        AuthVerifyUtils.mustAdmin();

        //非超管用户，只能建自己租户的组织
        if(!AuthVerifyUtils.isSuperAdmin() && !NumberUtil.equals(addOrgReq.getTenantId(), UserInfoContextUtils.getCurrentTenantId())){
            throw new ServiceException("只能创建当前租户的组织");
        }

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
        //非超管用户，只能修改自己租户的组织
        if(!AuthVerifyUtils.isSuperAdmin() && !NumberUtil.equals(old.getTenantId(), UserInfoContextUtils.getCurrentTenantId())){
            throw new ServiceException("只能修改当前租户的组织");
        }


        BOrganization pold = bOrganizationDao.getById(updateOrgReq.getParentOrganizationId());
        if(ObjectUtil.isNull(pold)){
            throw new ServiceException("上级组织结构不存在");
        }
        //非超管用户，只能修改自己租户的组织
        if(!AuthVerifyUtils.isSuperAdmin() && !NumberUtil.equals(pold.getTenantId(), UserInfoContextUtils.getCurrentTenantId())){
            throw new ServiceException("只能挂靠当前租户的组织");
        }

        BeanUtil.copyProperties(updateOrgReq, old);
        return bOrganizationDao.saveOrUpdate(old);
    }

    public Boolean delete(DeleteOrgReq deleteOrgReq){
        BOrganization old = bOrganizationDao.getById(deleteOrgReq.getOrganizationId());
        if(ObjectUtil.isNull(old)){
            throw new ServiceException("组织结构不存在");
        }

        //非超管用户，只能删除自己租户的组织
        if(!AuthVerifyUtils.isSuperAdmin() && !NumberUtil.equals(old.getTenantId(), UserInfoContextUtils.getCurrentTenantId())){
            throw new ServiceException("只能删除当前租户的组织");
        }


        if(bOrganizationDao.getNextLevelOrgCount(deleteOrgReq.getOrganizationId()) != 0L){
            throw new ServiceException("当前组织存在下级组织，不允许删除");
        }

        return bOrganizationDao.removeById(deleteOrgReq.getOrganizationId());
    }
}
