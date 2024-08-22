package com.kge.energy.crm.tenant.service;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.kge.energy.crm.common.util.RedisUtils;
import com.kge.energy.crm.repository.dao.BTenantDao;
import com.kge.energy.crm.repository.entity.BTenant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TenantDomainService {

    private final BTenantDao bTenantDao;

    private final String TENANT_NAME_CACHE_KEY = "tenant:name:";

    private final RedisUtils redisUtils;

    public String getTenantName(Integer tenantId) {

        String tenantName = redisUtils.get(TENANT_NAME_CACHE_KEY + tenantId);

        if (StrUtil.isBlank(tenantName)) {
            BTenant tenant = bTenantDao.getByIdWithDeleted(tenantId);
            Assert.notNull(tenant);

            redisUtils.setEx(TENANT_NAME_CACHE_KEY + tenantId, tenant.getName(), 24, TimeUnit.HOURS);

            return tenant.getName();
        }

        return tenantName;
    }

}
