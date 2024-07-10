package com.kge.energy.crm.user.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.RoleIdEnums;
import com.kge.energy.crm.repository.dao.BOrganizationDao;
import com.kge.energy.crm.repository.dao.BUserDao;
import com.kge.energy.crm.repository.entity.BOrganization;
import com.kge.energy.crm.repository.entity.BUser;
import com.kge.energy.crm.repository.entityext.result.RoleUserResult;
import com.kge.energy.crm.user.req.RoleUserReq;
import com.kge.energy.crm.user.resp.RoleUserResp;
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
public class UserService {

    private final BUserDao bUserDao;

    private final BOrganizationDao bOrganizationDao;

    public BUser getBUserById(int id) {
        return bUserDao.getById(id);
    }

    public UserInfoDto findUserInfoDto(BUser bUser) {

        UserInfoDto userInfoDto = bUserDao.findUserInfoDto(bUser.getUserId());
        if (ObjUtil.isNull(userInfoDto)) {
            return null;
        }

        List<UserInfoDto.Organization> orgs = bOrganizationDao.findUserInfoDtoOrOrgs(bUser.getUserId());
        userInfoDto.setOrganizationList(orgs);

        return userInfoDto;
    }

    public List<RoleUserResp> getUserByRoleId(RoleUserReq req) {

        Integer userId = UserInfoContextUtils.getCurrentUserId();

        List<RoleUserResult> roleUserResults;

        if (ObjUtil.equals(UserInfoContextUtils.getCurrentUserInfo().getRoleId(), RoleIdEnums.SYSTEM_ADMINISTRATOR.getCode())) {
            roleUserResults = bUserDao.getUserByRoleId(req.getRoleId());
        } else {
            BOrganization bOrganization = bOrganizationDao.getOrgByUserId(userId);
            Assert.notNull(bOrganization);
            roleUserResults = bUserDao.getUserByRoleAndOrgId(req.getRoleId(), bOrganization.getOrganizationId());
        }

        return BeanUtil.copyToList(roleUserResults, RoleUserResp.class);
    }
}
