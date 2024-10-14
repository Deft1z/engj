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
import com.kge.energy.crm.enums.RoleEnums;
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
import java.util.Collections;
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

        List<String> deleteLastTokenEnvs = List.of("dev", "test");
        if (!deleteLastTokenEnvs.contains(env) && deleteLastToken) {
            deleteLastToken(user, systemTypeEnum);
        }
        redisUtils.setEx(lastTokenKey, authToken, expiredTimeout + 2, expiredTimeUnit);

        return authToken;
    }

    /**
     * 删除用户上一个登录的token
     */
    public void deleteLastToken(BUser user, SystemTypeEnum systemTypeEnum) {

        String lastTokenKey = String.format(TokenConstant.LAST_TOKEN_CACHE_KEY, systemTypeEnum.getCode(), user.getUserId());
        String lastToken = redisUtils.get(lastTokenKey);

        if (StrUtil.isNotBlank(lastToken)) {
            redisUtils.delete(authProperties.getToken().getRedisFront() + lastToken);
        }
    }

    /**
     * 删除用户所有平台登录的token
     */
    public void deleteUserToken(BUser bUser) {

        for (SystemTypeEnum systemTypeEnum : SystemTypeEnum.values()) {
            deleteLastToken(bUser, systemTypeEnum);
        }
    }

    /**
     * 根据用户ID获取用户的联系方式
     *
     * @param userId
     * @param tenantId
     * @return
     */
    public List<UserContactDto> getUserContact(Integer userId, Integer tenantId) {
        List<BUser> userContact = bUserDao.getUserContact(userId, null, null, tenantId);
        return BeanUtil.copyToList(userContact, UserContactDto.class);
    }

    /**
     * 根据角色code获取用户的联系方式
     *
     * @param roleEnums
     * @param tenantId
     * @return
     */
    public List<UserContactDto> getUserContact(List<RoleEnums> roleEnums, Integer tenantId) {
        if (roleEnums.isEmpty()) {
            return Collections.emptyList();
        }
        //需移除小程序角色和二级公司客服，不然通过该方法可能会返回所有小程序客户和二级公司客服的联系方式，要避免发送消息给全部小程序客户和二级公司客服
        List<String> roleCods = roleEnums.stream()
                .filter(item -> !item.equals(RoleEnums.APPLET_USER) && !item.equals(RoleEnums.SUB_COMPANY_CUSTOMER))
                .map(RoleEnums::getCode).toList();
        List<BUser> userContact = bUserDao.getUserContact(null, roleCods, null, tenantId);
        return BeanUtil.copyToList(userContact, UserContactDto.class);
    }

    /**
     * 跟进角色code和组织ID获取用户的联系方式
     *
     * @param roleEnums
     * @param tenantId
     * @return
     */
    public List<UserContactDto> getUserContact(List<RoleEnums> roleEnums, Integer organizationId, Integer tenantId) {
        if (roleEnums.isEmpty() || organizationId == null) {
            return Collections.emptyList();
        }
        //需移除小程序角色，不然通过该方法可能会返回所有小程序客户的联系方式，要避免发送消息给全部小程序客户
        List<String> roleCods = roleEnums.stream()
                .filter(item -> !item.equals(RoleEnums.APPLET_USER))
                .map(RoleEnums::getCode).toList();
        List<BUser> userContact = bUserDao.getUserContact(null, roleCods, organizationId, tenantId);
        return BeanUtil.copyToList(userContact, UserContactDto.class);
    }

    public List<BUser> findByPhone(String phone) {
        return bUserDao.findByPhone(phone);
    }

    public String findShareUser(List<Integer> userIdList, Integer appid) {
        return bUserDao.findShareUser(userIdList, appid);
    }

}
