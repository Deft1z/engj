package com.kge.energy.crm.user.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.execption.BadException;
import com.kge.energy.crm.common.net.ResponseCode;
import com.kge.energy.crm.common.property.AuthProperties;
import com.kge.energy.crm.common.util.RedisUtils;
import com.kge.energy.crm.repository.dao.BOrganizationDao;
import com.kge.energy.crm.repository.dao.BUserDao;
import com.kge.energy.crm.repository.dao.LUserTokenDao;
import com.kge.energy.crm.repository.entity.BUser;
import com.kge.energy.crm.repository.entity.LUserToken;
import com.kge.energy.crm.tenant.service.TenantDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    private final RedisUtils redisUtils;

    @Value("${spring.profiles.active}")
    private String env;

    private final TenantDomainService tenantDomainService;

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
        userInfoDto.setTenantName(tenantDomainService.getTenantName(user.getTenantId()));
        userInfoDto.setSystemType(systemType);
        userInfoDto.setMobile(user.getMobile());
        userInfoDto.setWxOpenId(user.getOpenId());
        userInfoDto.setJobLevel(user.getJobLevel());

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
    public String genToken(@Nonnull BUser user, long expiredTimeout, TimeUnit expiredTimeUnit, boolean deleteLastToken) {

        String authToken = IdUtil.fastSimpleUUID();
        String authTokenKeyPrefix = authProperties.getToken().getRedisFront();

        LocalDateTime expiredTime = LocalDateTime.now().plusHours(121);

        redisUtils.setEx(authTokenKeyPrefix + authToken, String.valueOf(user.getUserId()), expiredTimeout, expiredTimeUnit);

        LUserToken lUserToken = lUserTokenDao.findByUid(user.getUserId());

        if (ObjectUtil.isNotNull(lUserToken)) {

            //如果是dev环境不删除旧的token
            if (!StrUtil.equals(env, "dev")) {
                redisUtils.delete(authProperties.getToken().getRedisFront() + lUserToken.getLoginToken());
            }

            lUserToken.setLoginToken(authToken)
                    .setLoginExpiredTime(expiredTime);
            lUserTokenDao.updateById(lUserToken);

            if (deleteLastToken) {
                redisUtils.delete(authTokenKeyPrefix + lUserToken.getLoginToken());
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
