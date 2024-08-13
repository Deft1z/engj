package com.kge.energy.crm.role.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.AuthVerifyUtils;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.repository.dao.BRoleDao;
import com.kge.energy.crm.repository.dao.RRoleResourceDao;
import com.kge.energy.crm.repository.entity.BRole;
import com.kge.energy.crm.repository.entity.RUserRole;
import com.kge.energy.crm.repository.entityext.param.RoleListParam;
import com.kge.energy.crm.role.req.*;
import com.kge.energy.crm.role.resp.RoleListResp;
import com.kge.energy.crm.role.resp.RoleResourceResp;
import com.kge.energy.crm.role.resp.UserRoleResp;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService {

    private final BRoleDao bRoleDao;

    private final RRoleResourceDao rRoleResourceDao;

    public PageResp<RoleListResp> list(RoleListReq req) {

        AuthVerifyUtils.mustAdmin();

        if (!AuthVerifyUtils.isSuperAdmin() && ObjUtil.notEqual(UserInfoContextUtils.getCurrentTenantId(), req.getTenantId())) {
            throw new ServiceException("非法请求，不允许查看其他租户角色");
        }

        RoleListParam param = BeanUtil.copyProperties(req, RoleListParam.class);

        Page<BRole> page = bRoleDao.selectPage(param);

        List<RoleListResp> roles = page.getRecords()
                .stream()
                .map(role -> new RoleListResp()
                        .setRoleId(role.getRoleId())
                        .setName(role.getName())
                        .setCode(role.getCode())
                        .setStatus(role.getStatus())
                        .setRemark(role.getRemark())
                ).collect(Collectors.toList());

        return new PageResp<RoleListResp>()
                .setList(roles)
                .setTotal(page.getTotal())
                .setPageSize(page.getSize())
                .setCurrentPage(page.getCurrent());
    }

    public Boolean add(AddRoleReq req) {

        AuthVerifyUtils.mustAdmin();

        if (!AuthVerifyUtils.isSuperAdmin() && ObjUtil.notEqual(UserInfoContextUtils.getCurrentTenantId(), req.getTenantId())) {
            throw new ServiceException("非法请求，不允许新增其他租户角色");
        }

        BRole bRole = new BRole()
                .setTenantId(req.getTenantId())
                .setName(req.getName())
                .setCode(req.getCode())
                .setStatus(req.getStatus())
                .setRemark(req.getRemark());

        return bRoleDao.save(bRole);
    }

    public Boolean update(UpdateRoleReq req) {

        AuthVerifyUtils.mustAdmin();

        BRole bRole = bRoleDao.getById(req.getRoleId());
        Assert.notNull(bRole, "角色不存在");

        if (!AuthVerifyUtils.isSuperAdmin() && ObjUtil.notEqual(UserInfoContextUtils.getCurrentTenantId(), bRole.getTenantId())) {
            throw new ServiceException("非法请求，不允许更新其他租户角色");
        }

        bRole.setName(req.getName())
                .setCode(req.getCode())
                .setStatus(req.getStatus())
                .setRemark(req.getRemark());

        return bRoleDao.updateById(bRole);
    }

    public Boolean delete(DeleteRoleReq req) {

        AuthVerifyUtils.mustAdmin();

        BRole bRole = bRoleDao.getById(req.getRoleId());
        Assert.notNull(bRole, "角色不存在");

        if (!AuthVerifyUtils.isSuperAdmin() && ObjUtil.notEqual(UserInfoContextUtils.getCurrentTenantId(), bRole.getTenantId())) {
            throw new ServiceException("非法请求，不允许删除其他租户角色");
        }

        boolean existedRoleUsers = new LambdaQueryChainWrapper<>(RUserRole.class)
                .eq(RUserRole::getRoleId, req.getRoleId()).exists();
        if (existedRoleUsers) {
            throw new ServiceException("当前角色存在绑定用户，不允许删除");
        }

        rRoleResourceDao.removeByRoleId(bRole.getRoleId());

        return bRoleDao.removeById(bRole);
    }

    /**
     * 角色已关联菜单
     */
    public RoleResourceResp roleResource(RoleResourceReq req) {

        AuthVerifyUtils.mustAdmin();

        List<Integer> resourceIdList = bRoleDao.roleResource(req.getRoleId(), req.getSystemType());

        return new RoleResourceResp()
                .setResourceIdList(resourceIdList);
    }

    public UserRoleResp userRole(UserRoleReq req) {

        AuthVerifyUtils.mustAdmin();

        List<BRole> bRoles = bRoleDao.userRole(req.getUserId());

        List<UserRoleResp.Role> roles = bRoles.stream()
                .map(role -> new UserRoleResp.Role()
                        .setName(role.getName())
                        .setRoleId(role.getRoleId()))
                .collect(Collectors.toList());

        return new UserRoleResp()
                .setRoles(roles);
    }
}
