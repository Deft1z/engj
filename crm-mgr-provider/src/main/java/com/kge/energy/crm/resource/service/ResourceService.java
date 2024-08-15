package com.kge.energy.crm.resource.service;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.kge.energy.crm.common.util.AuthVerifyUtils;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.repository.dao.BResourceDao;
import com.kge.energy.crm.repository.dao.RRoleResourceDao;
import com.kge.energy.crm.repository.entity.BResource;
import com.kge.energy.crm.repository.entity.RRoleResource;
import com.kge.energy.crm.resource.req.*;
import com.kge.energy.crm.resource.resp.ResourceListResp;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceDomainService resourceDomainService;

    private final BResourceDao bResourceDao;

    private final RRoleResourceDao rRoleResourceDao;


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

        return bResourceDao.save(bResource);
    }

    /**
     * 编辑菜单
     */
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

        return bResourceDao.updateById(bResource);
    }

    /**
     * 删除菜单
     */
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

        return bResourceDao.removeById(req.getResourceId());
    }
}
