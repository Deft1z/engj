package com.kge.energy.crm.tenant.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.AuthVerifyUtils;
import com.kge.energy.crm.common.util.RedisUtils;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.OperateModuleEnums;
import com.kge.energy.crm.log.service.SysOperateLogService;
import com.kge.energy.crm.repository.dao.BTenantDao;
import com.kge.energy.crm.repository.entity.BTenant;
import com.kge.energy.crm.repository.entityext.param.TenantQueryParam;
import com.kge.energy.crm.repository.entityext.result.TenantListForOrgResult;
import com.kge.energy.crm.repository.entityext.result.TenantListResult;
import com.kge.energy.crm.tenant.req.AddTenantReq;
import com.kge.energy.crm.tenant.req.DeleteTenantReq;
import com.kge.energy.crm.tenant.req.QueryTenantReq;
import com.kge.energy.crm.tenant.req.UpdateTenantReq;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TenantService {

    private final BTenantDao bTenantDao;

    private final String TENANT_NAME_CACHE_KEY = "tenant:name";

    private final RedisUtils redisUtils;

    private final SysOperateLogService sysOperateLogService;

    public PageResp<TenantListResult> selectPage(QueryTenantReq req) {
        AuthVerifyUtils.isSuperAdmin();
        TenantQueryParam param = BeanUtil.copyProperties(req, TenantQueryParam.class);
        return new PageResp<>(bTenantDao.selectTenantPage(param));
    }

    @Transactional
    public Boolean add(AddTenantReq req) {
        AuthVerifyUtils.isSuperAdmin();
        BTenant bTenant = BeanUtil.copyProperties(req, BTenant.class);

        bTenantDao.save(bTenant);

        sysOperateLogService.saveLog(
                bTenant.getId(), OperateModuleEnums.TENANT,
                "新增租户【" + bTenant.getId() + ", " + bTenant.getName() + "】"
        );

        return true;
    }

    @Transactional
    public Boolean update(UpdateTenantReq req) {
        AuthVerifyUtils.isSuperAdmin();
        BTenant old = bTenantDao.getById(req.getId());
        if (ObjectUtil.isNull(old)) {
            throw new ServiceException("租户不存在");
        }

        BeanUtil.copyProperties(req, old);
        bTenantDao.saveOrUpdate(old);

        sysOperateLogService.saveLog(
                old.getId(), OperateModuleEnums.TENANT,
                "更新租户【" + old.getId() + ", " + old.getName() + "】"
        );

        return true;
    }

    @Transactional
    public Boolean delete(DeleteTenantReq req) {
        AuthVerifyUtils.isSuperAdmin();
        BTenant old = bTenantDao.getById(req.getId());
        if (ObjectUtil.isNull(old)) {
            throw new ServiceException("租户不存在");
        }

        bTenantDao.removeById(req.getId());

        sysOperateLogService.saveLog(
                old.getId(), OperateModuleEnums.TENANT,
                "删除租户【" + old.getId() + ", " + old.getName() + "】"
        );

        return true;
    }

    public List<TenantListForOrgResult> getTenantDictList() {
        AuthVerifyUtils.mustAdmin();
        return AuthVerifyUtils.isSuperAdmin() ?
                bTenantDao.getTenantDictList(null) :
                bTenantDao.getTenantDictList(UserInfoContextUtils.getCurrentTenantId());
    }

    public String getTenantName(Integer tenantId) {

        String tenantName = redisUtils.get(TENANT_NAME_CACHE_KEY + tenantId);

        if (StrUtil.isBlank(tenantName)) {
            BTenant tenant = new LambdaQueryChainWrapper<>(BTenant.class)
                    .eq(BTenant::getId, tenantId)
                    .in(BTenant::getFlag, List.of(1, -1))
                    .one();
            bTenantDao.getById(tenantId);
            Assert.notNull(tenant);

            redisUtils.setEx(TENANT_NAME_CACHE_KEY + tenantId, tenant.getName(), 24, TimeUnit.HOURS);

            return tenant.getName();
        }

        return tenantName;
    }

}
