package com.kge.energy.crm.user.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.kge.energy.crm.common.constans.TokenConstant;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.net.ResponseCode;
import com.kge.energy.crm.common.property.AuthProperties;
import com.kge.energy.crm.common.util.RedisUtils;
import com.kge.energy.crm.enums.SystemTypeEnum;
import com.kge.energy.crm.repository.dao.BOrganizationDao;
import com.kge.energy.crm.repository.dao.BUserDao;
import com.kge.energy.crm.repository.entity.BUser;
import com.kge.energy.crm.tenant.service.TenantDomainService;
import com.kge.energy.msg.dto.UserContactDto;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
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

    private final RedisUtils redisUtils;

    @Value("${spring.profiles.active}")
    private String env;

    private final TenantDomainService tenantDomainService;

    public UserInfoDto findUserInfoDto(String systemType, Integer userId) {

        BUser user = bUserDao.getById(userId);
        if (ObjUtil.isNull(user)) {
            throw new ServiceException(ResponseCode.TOKEN_FAIL.getCode(), ResponseCode.TOKEN_FAIL.getMsg());
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

        userInfoDto.setRoleIds(userRoles.stream().map(UserInfoDto.Role::getId).collect(Collectors.toSet()));
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
    public String genToken(@Nonnull BUser user, SystemTypeEnum systemTypeEnum,
                           long expiredTimeout, TimeUnit expiredTimeUnit, boolean deleteLastToken) {

        String authToken = IdUtil.fastSimpleUUID();
        String authTokenKeyPrefix = authProperties.getToken().getRedisFront();

        redisUtils.setEx(authTokenKeyPrefix + authToken, String.valueOf(user.getUserId()), expiredTimeout, expiredTimeUnit);

        String lastTokenKey = String.format(TokenConstant.LAST_TOKEN_CACHE_KEY, systemTypeEnum.getCode(), user.getUserId());
        String lastToken = redisUtils.get(lastTokenKey);

        if (StrUtil.isNotBlank(lastToken)) {
            //如果是dev环境不删除旧的token
            if (!StrUtil.equals(env, "dev") && deleteLastToken) {
                redisUtils.delete(authTokenKeyPrefix + lastToken);
            }
        }

        redisUtils.setEx(lastTokenKey, authToken, expiredTimeout + 2, expiredTimeUnit);

        return authToken;
    }

    public List<UserContactDto> getUserContact(Integer userId, String roleCode, Integer organizationId, Integer tenantId) {
        List<BUser> userContact = bUserDao.getUserContact(userId, roleCode, organizationId, tenantId);
        return BeanUtil.copyToList(userContact, UserContactDto.class);
    }

    public List<BUser> findByPhone(String phone) {
        return bUserDao.findByPhone(phone);
    }

    public String findShareUser(List<Integer> userIdList, Integer appid) {
        return bUserDao.findShareUser(userIdList, appid);
    }

}
