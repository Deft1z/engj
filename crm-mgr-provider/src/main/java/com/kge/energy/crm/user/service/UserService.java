package com.kge.energy.crm.user.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.execption.BadException;
import com.kge.energy.crm.common.net.ResponseCode;
import com.kge.energy.crm.common.property.AuthProperties;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.RoleIdEnums;
import com.kge.energy.crm.repository.dao.*;
import com.kge.energy.crm.repository.entity.*;
import com.kge.energy.crm.repository.entityext.result.RoleUserResult;
import com.kge.energy.crm.user.req.RoleUserReq;
import com.kge.energy.crm.user.req.UserLoginReq;
import com.kge.energy.crm.user.req.UserSaltReq;
import com.kge.energy.crm.user.req.WxUserListReq;
import com.kge.energy.crm.user.resp.CurrentUserInfoResp;
import com.kge.energy.crm.user.resp.RoleUserResp;
import com.kge.energy.crm.user.resp.UserLoginResp;
import com.kge.energy.crm.user.resp.WxUserListResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final BUserDao bUserDao;

    private final BOrganizationDao bOrganizationDao;

    private final SSystemConfigDao sSystemConfigDao;

    private final RUserTenantDao rUserTenantDao;

    private final AuthProperties authProperties;

    private final LUserTokenDao lUserTokenDao;

    private final StringRedisTemplate stringRedisTemplate;

    @Value("${spring.profiles.active}")
    private String env;

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

    public BUser getUserByMobile(String mobile) {
        return bUserDao.getUserByMobile(mobile);
    }

    public List<RoleUserResp> getUserByRoleId(RoleUserReq req) {

        Integer userId = UserInfoContextUtils.getCurrentUserId();

        List<RoleUserResult> roleUserResults;


        if (UserInfoContextUtils.getCurrentUserInfo().getRoleList().stream().anyMatch(role -> ObjUtil.equals(role.getId(), RoleIdEnums.SYSTEM_ADMINISTRATOR.getCode()))) {
            roleUserResults = bUserDao.getUserByRoleId(req.getRoleId());
        } else {
            BOrganization bOrganization = bOrganizationDao.getOrgByUserId(userId);
            Assert.notNull(bOrganization);
            roleUserResults = bUserDao.getUserByRoleAndOrgId(req.getRoleId(), bOrganization.getOrganizationId());
        }

        return BeanUtil.copyToList(roleUserResults, RoleUserResp.class);
    }

    public String userSalt(UserSaltReq req) {

        BUser bUser = bUserDao.findOneByName(req.getName());

        if (ObjUtil.isNotNull(bUser)) {
            return bUser.getPasswdSalt();
        }

        // 找不到name到系统表中获取盐值，md5(username+salt),确保相同的name获取相同的盐值,防止通过盐值测试用户名是否存在
        SSystemConfig sSystemConfig = sSystemConfigDao.findByName("SALTBASE");

        return MD5.create().digestHex(req.getName() + sSystemConfig.getConfig());
    }

    public UserLoginResp userLogin(UserLoginReq req) {

        BUser bUser = bUserDao.getOne(Wrappers.lambdaQuery(new BUser().setName(req.getName()).setPasswd(req.getPasswd())));

        if (ObjUtil.isNull(bUser)) {
            throw new BadException(ResponseCode.SHOULD_LOGIN);
        }

        // 获取uid关联的租户
        RUserTenant rUserTenant = rUserTenantDao.findTenantByUid(bUser.getUserId());

        String authToken = genToken(bUser);

        return new UserLoginResp()
                .setUserId(bUser.getUserId())
                .setTenantId(rUserTenant.getOrganizationId())
                .setAuthToken(authToken)
                .setMsg("login success");
    }

    public CurrentUserInfoResp currentUserInfo() {

        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();

        if (ObjUtil.isNull(userInfoDto)) {
            throw new BadException(ResponseCode.TOKEN_FAIL);
        }

        if (CollectionUtil.isEmpty(userInfoDto.getOrganizationList())) {
            throw new BadException("找不到用户对应的租户ID");
        }

        List<CurrentUserInfoResp.OrganizationListBean> orgs = userInfoDto.getOrganizationList()
                .stream()
                .map(item -> new CurrentUserInfoResp.OrganizationListBean()
                        .setId(item.getId())
                        .setName(item.getName())
                        .setAuthCode(item.getAuthCode())
                ).toList();

        return new CurrentUserInfoResp()
                .setUserId(Math.toIntExact(userInfoDto.getUserId()))
                .setUserName(userInfoDto.getUserName())
                .setUserName(userInfoDto.getUserName())
                // todo
                .setRoleId(userInfoDto.getRoleList().get(0).getId())
                .setRoleName(userInfoDto.getRoleList().get(0).getName())
                .setOrganizationList(orgs);
    }

    public String genToken(@Nonnull BUser user) {
        String authToken = IdUtil.fastSimpleUUID();
        LocalDateTime expiredTime = LocalDateTime.now().plusHours(121);

        stringRedisTemplate.opsForValue()
                .set(authProperties.getToken().getRedisFront() + authToken, String.valueOf(user.getUserId()), 121, TimeUnit.HOURS);

        LUserToken lUserToken = lUserTokenDao.findByUid(user.getUserId());

        if (ObjUtil.isNotNull(lUserToken) && ObjUtil.notEqual(lUserToken.getUserTokenId(), 0)) {

            //如果是dev环境不删除旧的token
            if(!StrUtil.equals(env, "dev")) {
                stringRedisTemplate.delete(authProperties.getToken().getRedisFront() + lUserToken.getLoginToken());
            }

            lUserToken.setLoginToken(authToken)
                    .setLoginExpiredTime(expiredTime);
            lUserTokenDao.updateById(lUserToken);
        } else {
            lUserTokenDao.save(new LUserToken()
                    .setUserId(user.getUserId())
                    .setLoginExpiredTime(expiredTime)
                    .setLoginToken(authToken)
            );
        }
        return authToken;
    }

    public WxUserListResp findWxUserList(WxUserListReq req) {
        IPage<BUser> users = bUserDao.findAllWxUser(req.getSearchMap().getName(), req.getCurrentPage(), req.getPageSize());
        return new WxUserListResp(users.getCurrent(), users.getSize(), users.getTotal(), users.getRecords());
    }

}
