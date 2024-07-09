package com.kge.energy.crm.user.service;

import cn.hutool.core.util.ObjUtil;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.repository.dao.BOrganizationDao;
import com.kge.energy.crm.repository.dao.BUserDao;
import com.kge.energy.crm.repository.entity.BUser;
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
public class BUserService {

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
}
