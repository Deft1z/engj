package com.kge.energy.crm.user.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjUtil;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.execption.BadException;
import com.kge.energy.crm.common.net.ResponseCode;
import com.kge.energy.crm.common.property.AuthProperties;
import com.kge.energy.crm.repository.dao.BOrganizationDao;
import com.kge.energy.crm.repository.dao.BUserDao;
import com.kge.energy.crm.repository.dao.LUserTokenDao;
import com.kge.energy.crm.repository.entity.BUser;
import com.kge.energy.crm.repository.entity.LUserToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author wangjihua
 */
@Service
@RequiredArgsConstructor
public class UserDomainService {

    private final AuthProperties authProperties;

    private final BUserDao bUserDao;

    private final BOrganizationDao bOrganizationDao;

    private final LUserTokenDao lUserTokenDao;

    private final StringRedisTemplate stringRedisTemplate;

    public BUser getBUserById(int id) {
        return bUserDao.getById(id);
    }

    public UserInfoDto findUserInfoDto(String systemType, Integer userId) {

        BUser user = bUserDao.getById(userId);
        if (ObjUtil.isNull(user)) {
            throw new BadException(ResponseCode.TOKEN_FAIL);
        }

        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setUserId(Long.valueOf(user.getUserId()));
        userInfoDto.setUserName(user.getName());
        userInfoDto.setRealname(user.getRealname());
        userInfoDto.setTenantId(user.getTenantId());
        userInfoDto.setSystemType(systemType);
        userInfoDto.setMobile(user.getMobile());
        userInfoDto.setWxOpenId(user.getOpenId());

        List<UserInfoDto.Role> userRoles = bUserDao.getUserRoles(systemType, user.getUserId());
        userInfoDto.setRoleList(userRoles);

        userInfoDto.setRoleCodes(userRoles.stream().map(UserInfoDto.Role::getCode).collect(Collectors.toSet()));

        List<UserInfoDto.Organization> orgs = bOrganizationDao.findUserInfoDtoOrgs(user.getUserId());
        userInfoDto.setOrganizationList(orgs);

        return userInfoDto;
    }

    /**
     * 生成用户认证令牌。
     *
     * @param user            用户信息，不能为空。
     * @param deleteLastToken 是否删除用户之前的令牌。
     * @return 新生成的认证令牌。
     */
    public String genToken(@Nonnull BUser user, boolean deleteLastToken) {

        String authToken = IdUtil.fastSimpleUUID();
        String authTokenKeyPrefix = authProperties.getToken().getRedisFront();

        LocalDateTime expiredTime = LocalDateTime.now().plusHours(121);

        stringRedisTemplate.opsForValue()
                .set(authTokenKeyPrefix + authToken, String.valueOf(user.getUserId()), 121, TimeUnit.HOURS);

        LUserToken lUserToken = lUserTokenDao.findByUid(user.getUserId());

        if (ObjUtil.isNotNull(lUserToken) && ObjUtil.notEqual(lUserToken.getUserTokenId(), 0)) {

            lUserToken.setLoginToken(authToken)
                    .setLoginExpiredTime(expiredTime);
            lUserTokenDao.updateById(lUserToken);

            if (deleteLastToken) {
                stringRedisTemplate.delete(authTokenKeyPrefix + lUserToken.getLoginToken());
            }

        } else {
            lUserTokenDao.save(new LUserToken()
                    .setUserId(user.getUserId())
                    .setLoginExpiredTime(expiredTime)
                    .setLoginToken(authToken)
            );
        }
        return authToken;
    }
}
