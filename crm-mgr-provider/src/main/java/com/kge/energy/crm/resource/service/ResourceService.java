package com.kge.energy.crm.resource.service;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.kge.energy.crm.common.util.AuthVerifyUtils;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.OperateModuleEnums;
import com.kge.energy.crm.log.service.SysOperateLogService;
import com.kge.energy.crm.repository.dao.BResourceDao;
import com.kge.energy.crm.repository.entity.BResource;
import com.kge.energy.crm.repository.entity.RRoleResource;
import com.kge.energy.crm.resource.req.*;
import com.kge.energy.crm.resource.resp.ResourceListResp;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceDomainService resourceDomainService;

    private final BResourceDao bResourceDao;

    private final SysOperateLogService sysOperateLogService;


    /**
     * 当前登录用户菜单
     */
    public ResourceListResp currentUserResource() {

        UserResourceReq req = new UserResourceReq()
                .setUserId(UserInfoContextUtils.getCurrentUserId())
                .setSystemType(UserInfoContextUtils.getCurrentSystemType())
                .setTenantId(UserInfoContextUtils.getCurrentTenantId());

        return resourceDomainService.getUserResources(req);
    }

    /**
     * 获取系统所有菜单资源
     */
    public ResourceListResp systemResources(SystemResourceReq req) {

        AuthVerifyUtils.mustAdmin();

        return resourceDomainService.getSystemResources(req);
    }

    /**
     * 新增菜单
     */
    @Transactional
    public Boolean add(AddResourceReq req) {

        AuthVerifyUtils.mustAdmin();

        BResource bResource = new BResource()
                .setParentResourceId(req.getParentResourceId())
                .setResourceName(req.getResourceName())
                .setResourceCode(req.getResourceCode())
                .setResourceType(req.getResourceType())
                .setSort(req.getSort())
                .setPath(req.getPath())
                .setPathType(req.getPathType())
                .setIconCode(req.getIconCode())
                .setIconFilePath(req.getIconFilePath())
                .setStatus(req.getStatus())
                .setSystemType(req.getSystemType())
                .setRemark(req.getRemark());

        bResourceDao.save(bResource);

        sysOperateLogService.saveLog(
                UserInfoContextUtils.getCurrentTenantId(), OperateModuleEnums.RESOURCE,
                "新增菜单【" + bResource.getResourceId() + ", " + bResource.getResourceName() + "】"
        );

        return true;
    }

    /**
     * 编辑菜单
     */
    @Transactional
    public Boolean update(UpdateResourceReq req) {

        AuthVerifyUtils.mustAdmin();

        BResource bResource = bResourceDao.getById(req.getResourceId());
        Assert.notNull(bResource, "菜单不存在");

        bResource.setParentResourceId(req.getParentResourceId())
                .setResourceName(req.getResourceName())
                .setResourceCode(req.getResourceCode())
                .setResourceType(req.getResourceType())
                .setSort(req.getSort())
                .setPath(req.getPath())
                .setPathType(req.getPathType())
                .setIconCode(req.getIconCode())
                .setIconFilePath(req.getIconFilePath())
                .setStatus(req.getStatus())
                .setSystemType(req.getSystemType())
                .setRemark(req.getRemark());

        bResourceDao.updateById(bResource);

        sysOperateLogService.saveLog(
                UserInfoContextUtils.getCurrentTenantId(), OperateModuleEnums.RESOURCE,
                "更新菜单【" + bResource.getResourceId() + ", " + bResource.getResourceName() + "】"
        );

        return true;
    }

    /**
     * 删除菜单
     */
    @Transactional
    public Boolean delete(DeleteResourceReq req) {

        AuthVerifyUtils.mustAdmin();

        BResource bResource = bResourceDao.getById(req.getResourceId());
        Assert.notNull(bResource, "菜单不存在");

        boolean existsRoleResource = new LambdaQueryChainWrapper<>(RRoleResource.class)
                .eq(RRoleResource::getResourceId, req.getResourceId())
                .exists();
        if (existsRoleResource) {
            throw new ServiceException("已有角色绑定该菜单，不允许删除");
        }

        bResourceDao.removeById(req.getResourceId());

        sysOperateLogService.saveLog(
                UserInfoContextUtils.getCurrentTenantId(), OperateModuleEnums.RESOURCE,
                "删除菜单【" + bResource.getResourceId() + ", " + bResource.getResourceName() + "】"
        );

        return true;
    }
}
