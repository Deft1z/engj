package com.kge.energy.crm.user.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.execption.BadException;
import com.kge.energy.crm.common.net.ResponseCode;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.RoleIdEnums;
import com.kge.energy.crm.repository.dao.BOrganizationDao;
import com.kge.energy.crm.repository.dao.BUserDao;
import com.kge.energy.crm.repository.dao.RUserTenantDao;
import com.kge.energy.crm.repository.dao.SSystemConfigDao;
import com.kge.energy.crm.repository.entity.BOrganization;
import com.kge.energy.crm.repository.entity.BUser;
import com.kge.energy.crm.repository.entity.RUserTenant;
import com.kge.energy.crm.repository.entity.SSystemConfig;
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

    private final SSystemConfigDao sSystemConfigDao;

    private final RUserTenantDao rUserTenantDao;

    private final UserDomainService userDomainService;

    public BUser getUserByMobile(String mobile) {
        return bUserDao.getUserByMobile(mobile);
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

        String authToken = userDomainService.genToken(bUser, true);

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
                .setRoleId(userInfoDto.getRoleId())
                .setRoleName(userInfoDto.getRoleName())
                .setOrganizationList(orgs);
    }

    public WxUserListResp findWxUserList(WxUserListReq req) {
        IPage<BUser> users = bUserDao.findAllWxUser(req.getSearchMap().getName(), req.getCurrentPage(), req.getPageSize());
        return new WxUserListResp(users.getCurrent(), users.getSize(), users.getTotal(), users.getRecords());
    }


}
