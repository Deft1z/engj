package com.kge.energy.crm.tenant.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.repository.dao.BTenantDao;
import com.kge.energy.crm.repository.entity.BTenant;
import com.kge.energy.crm.repository.entityext.param.TenantQueryParam;
import com.kge.energy.crm.repository.entityext.result.TenantListResult;
import com.kge.energy.crm.tenant.req.AddTenantReq;
import com.kge.energy.crm.tenant.req.DeleteTenantReq;
import com.kge.energy.crm.tenant.req.QueryTenantReq;
import com.kge.energy.crm.tenant.req.UpdateTenantReq;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TenantService {

    private final BTenantDao bTenantDao;

    public PageResp<TenantListResult> selectPage(QueryTenantReq req){
        TenantQueryParam param = BeanUtil.copyProperties(req, TenantQueryParam.class);
        return new PageResp<>(bTenantDao.selectPage(param));
    }

    public Boolean add(AddTenantReq req) {
        BTenant bTenant = BeanUtil.copyProperties(req, BTenant.class);
        return bTenantDao.save(bTenant);
    }

    public Boolean update(UpdateTenantReq req) {
        BTenant old = bTenantDao.getById(req.getId());
        if(ObjectUtil.isNull(old)){
            throw new ServiceException("租户不存在");
        }

        BeanUtil.copyProperties(req, old);
        return bTenantDao.saveOrUpdate(old);
    }

    public Boolean delete(DeleteTenantReq req) {
        BTenant old = bTenantDao.getById(req.getId());
        if(ObjectUtil.isNull(old)){
            throw new ServiceException("租户不存在");
        }

        bTenantDao.logicDelete(req.getId());
        return true;
    }

}
