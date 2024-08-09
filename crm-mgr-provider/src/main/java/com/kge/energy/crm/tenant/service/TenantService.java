package com.kge.energy.crm.tenant.service;

import cn.hutool.core.bean.BeanUtil;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.repository.dao.BTenantDao;
import com.kge.energy.crm.repository.entityext.param.TenantQueryParam;
import com.kge.energy.crm.repository.entityext.result.TenantListResult;
import com.kge.energy.crm.tenant.req.QueryTenantReq;
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

}
