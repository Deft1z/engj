package com.kge.energy.crm.user.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.execption.BadException;
import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.common.net.ResponseCode;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.property.AuthProperties;
import com.kge.energy.crm.common.util.AuthVerifyUtils;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.*;
import com.kge.energy.crm.log.service.SysOperateLogService;
import com.kge.energy.crm.login.SysLoginLogHandleService;
import com.kge.energy.crm.repository.dao.*;
import com.kge.energy.crm.repository.entity.*;
import com.kge.energy.crm.repository.entityext.param.UserListParam;
import com.kge.energy.crm.repository.entityext.result.RoleUserResult;
import com.kge.energy.crm.repository.entityext.result.UserListResult;
import com.kge.energy.crm.tenant.service.TenantDomainService;
import com.kge.energy.crm.user.req.*;
import com.kge.energy.crm.user.resp.*;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    private final RUserRoleDao rUserRoleDao;

    private final AuthProperties authProperties;

    private final LUserTokenDao lUserTokenDao;

    private final StringRedisTemplate stringRedisTemplate;

    private final BRoleDao bRoleDao;

    private final SysOperateLogService sysOperateLogService;

    private final TenantDomainService tenantDomainService;

    private final SysLoginLogHandleService sysLoginLogHandleService;

    @Value("${spring.profiles.active}")
    private String env;

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

    public CommonResponse<UserLoginResp> userLogin(UserLoginReq req) {
        UserLoginResp userLoginResp = null;
        BUser bUser = null;

        try {

            bUser = bUserDao.getOne(Wrappers.lambdaQuery(new BUser().setName(req.getName()).setPasswd(req.getPasswd())));

            if (ObjUtil.isNull(bUser)) {
                throw new BadException(ResponseCode.SHOULD_LOGIN);
            }

            // 获取uid关联的租户
            RUserTenant rUserTenant = rUserTenantDao.findTenantByUid(bUser.getUserId());

            String authToken = genToken(bUser);

            userLoginResp = new UserLoginResp()
                    .setUserId(bUser.getUserId())
                    .setTenantId(rUserTenant.getOrganizationId())
                    .setAuthToken(authToken)
                    .setMsg("login success");

            //记录登录成功日志
            sysLoginLogHandleService.saveLoginLog(bUser, LoginPlatformEnums.PC, LoginResultEnums.SUCCESS, null);

            return CommonResponse.suc(userLoginResp);

        } catch (Exception e) {
            log.error("pc login error: ", e);

            //记录登录失败日志
            sysLoginLogHandleService.saveLoginLog(bUser, LoginPlatformEnums.PC, LoginResultEnums.FAIL, e.toString());

            return CommonResponse.bad(ResponseCode.SHOULD_LOGIN, userLoginResp);
        }

    }

    public CurrentUserInfoResp currentUserInfo() {

        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();

        if (ObjUtil.isNull(userInfoDto)) {
            throw new BadException(ResponseCode.TOKEN_FAIL);
        }

        if (CollectionUtil.isEmpty(userInfoDto.getOrganizationList())) {
            throw new BadException("找不到用户对应的租户ID");
        }

        List<CurrentUserInfoResp.Role> roles = userInfoDto.getRoleList()
                .stream()
                .map(item -> new CurrentUserInfoResp.Role()
                        .setId(item.getId())
                        .setName(item.getName())
                        .setCode(item.getCode())
                ).toList();

        List<CurrentUserInfoResp.OrganizationListBean> orgs = userInfoDto.getOrganizationList()
                .stream()
                .map(item -> new CurrentUserInfoResp.OrganizationListBean()
                        .setId(item.getId())
                        .setName(item.getName())
                ).toList();

        return new CurrentUserInfoResp()
                .setUserId(Math.toIntExact(userInfoDto.getUserId()))
                .setUserName(userInfoDto.getUserName())
                .setUserName(userInfoDto.getUserName())
                .setRoleList(roles)
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
            if (!StrUtil.equals(env, "dev")) {
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

    /**
     * 获取租户或部门下的用户列表
     */
    public PageResp<UserListResp> list(UserListReq req) {

        AuthVerifyUtils.mustAdmin();

        if (AuthVerifyUtils.notSuperAdmin()) {
            req.setTenantId(UserInfoContextUtils.getCurrentTenantId());
        }

        UserListParam userListParam = BeanUtil.copyProperties(req, UserListParam.class);
        IPage<UserListResult> usersPage = bUserDao.list(userListParam);

        List<UserListResp> resps = usersPage.getRecords()
                .stream()
                .map(user -> new UserListResp()
                        .setUserId(user.getUserId())
                        .setName(user.getName())
                        .setRealname(user.getRealname())
                        .setMobile(user.getMobile())
                        .setStatus(user.getStatus())
                        .setOrganizationName(user.getOrganizationName())
                ).collect(Collectors.toList());

        return new PageResp<UserListResp>()
                .setCurrentPage(usersPage.getCurrent())
                .setTotal(usersPage.getTotal())
                .setPageSize(usersPage.getSize())
                .setList(resps);
    }


    /**
     * 用户详情
     */
    public UserDetailResp detail(Integer userId) {

        AuthVerifyUtils.mustAdmin();

        BUser bUser = bUserDao.getById(userId);
        Assert.notNull(bUser, "用户不存在");

        if (AuthVerifyUtils.notSuperAdmin() && ObjUtil.notEqual(UserInfoContextUtils.getCurrentTenantId(), bUser.getTenantId())) {
            throw new ServiceException("非法请求，不允许查看其他租户用户");
        }

        String tenantName = tenantDomainService.getTenantName(bUser.getTenantId());

        BOrganization bOrganization = bOrganizationDao.getOrgByUserId(userId);
        Assert.notNull(bOrganization);

        return new UserDetailResp()
                .setTenantId(bUser.getTenantId())
                .setTenantName(tenantName)
                .setOrganizationId(bOrganization.getOrganizationId())
                .setOrganizationName(bOrganization.getName())
                .setUserId(bUser.getUserId())
                .setName(bUser.getName())
                .setRealname(bUser.getRealname())
                .setMobile(bUser.getMobile())
                .setStatus(bUser.getStatus())
                .setRemark(bUser.getRemark());
    }

    @Transactional
    public Boolean add(AddUserReq req) {

        AuthVerifyUtils.mustAdmin();

        if (AuthVerifyUtils.notSuperAdmin() && ObjUtil.notEqual(UserInfoContextUtils.getCurrentTenantId(), req.getTenantId())) {
            throw new ServiceException("非法请求，不允许添加其他租户用户");
        }

        checkMobile(req.getMobile());

        checkExistedUser(req);

        BUser bUser = new BUser()
                .setTenantId(req.getTenantId())
                .setName(req.getName())
                .setRealname(req.getRealname())
                .setMobile(req.getMobile())
                .setPasswd(req.getPasswd())
                .setPasswdSalt(req.getPasswdSalt())
                .setStatus(req.getStatus())
                .setRemark(req.getRemark());
        bUserDao.save(bUser);

        RUserTenant rUserTenant = new RUserTenant()
                .setUserId(bUser.getUserId())
                .setOrganizationId(req.getOrganizationId())
                .setTenantId(req.getTenantId());
        rUserTenantDao.save(rUserTenant);

        sysOperateLogService.saveLog(
                bUser.getTenantId(), OperateModuleEnums.USER,
                "新增用户【" + bUser.getUserId() + ", " + bUser.getName() + ", " + bUser.getRealname() + "】"
        );

        return true;
    }

    /*
     * 编辑用户
     */
    @Transactional
    public Boolean update(UpdateUserReq req) {

        checkMobile(req.getMobile());

        AuthVerifyUtils.mustAdmin();

        BUser bUser = bUserDao.getById(req.getUserId());
        Assert.notNull(bUser);

        if (AuthVerifyUtils.notSuperAdmin() && ObjUtil.notEqual(UserInfoContextUtils.getCurrentTenantId(), bUser.getTenantId())) {
            throw new ServiceException("非法请求，不允许编辑其他租户用户");
        }

        bUser.setTenantId(req.getTenantId())
                .setName(req.getName())
                .setRealname(req.getRealname())
                .setMobile(req.getMobile())
                .setStatus(req.getStatus())
                .setRemark(req.getRemark());
        bUserDao.updateById(bUser);

        RUserTenant rUserTenant = rUserTenantDao.findTenantByUid(req.getUserId());
        if (ObjUtil.notEqual(req.getOrganizationId(), rUserTenant.getOrganizationId())) {
            rUserTenantDao.removeById(rUserTenant);
            rUserTenantDao.save(new RUserTenant()
                    .setUserId(bUser.getUserId())
                    .setOrganizationId(req.getOrganizationId())
                    .setTenantId(req.getTenantId()));
        }

        sysOperateLogService.saveLog(
                bUser.getTenantId(), OperateModuleEnums.USER,
                "更新用户【" + bUser.getUserId() + ", " + bUser.getName() + ", " + bUser.getRealname() + "】"
        );

        return true;
    }

    /**
     * 删除用户
     */
    @Transactional
    public Boolean delete(DeleteUserReq req) {

        AuthVerifyUtils.mustAdmin();

        BUser bUser = bUserDao.getById(req.getUserId());
        Assert.notNull(bUser);

        if (AuthVerifyUtils.notSuperAdmin() && ObjUtil.notEqual(UserInfoContextUtils.getCurrentTenantId(), bUser.getTenantId())) {
            throw new ServiceException("非法请求，不允许删除其他租户用户");
        }

        bUserDao.removeById(bUser);
        rUserTenantDao.removeByUserId(bUser.getUserId());
        rUserRoleDao.removeByUserId(bUser.getUserId());

        sysOperateLogService.saveLog(
                bUser.getTenantId(), OperateModuleEnums.USER,
                "删除用户【" + bUser.getUserId() + ", " + bUser.getName() + ", " + bUser.getRealname() + "】"
        );

        return true;
    }

    /**
     * 分配用户角色
     */
    @Transactional
    public Boolean assignRole(AssignUserRoleReq req) {

        AuthVerifyUtils.mustAdmin();

        BUser bUser = bUserDao.getById(req.getUserId());
        Assert.notNull(bUser);

        if (AuthVerifyUtils.notSuperAdmin() && ObjUtil.notEqual(UserInfoContextUtils.getCurrentTenantId(), bUser.getTenantId())) {
            throw new ServiceException("非法请求，不允分配其他租户用户角色");
        }

        List<BRole> bRoles = bRoleDao.listByIds(req.getRoleIds());
        boolean isCurrentTenantRole = bRoles.stream()
                .allMatch(brole -> ObjUtil.equals(bUser.getTenantId(), brole.getTenantId()));
        if (AuthVerifyUtils.notSuperAdmin() && !isCurrentTenantRole) {
            throw new ServiceException("非法请求，不允分配其他租户用户角色");
        }

        rUserRoleDao.removeByUserId(bUser.getUserId());

        // 限定不能同时拥有2种业务角色，后续有需要可考虑是否通过角色类型判断，目前先通过角色编码判断
        List<String> businessCodes = List.of(RoleEnums.APPLET_USER.getCode(), RoleEnums.JT_CUSTOMER.getCode(), RoleEnums.SUB_COMPANY_CUSTOMER.getCode());
        List<String> roleCodes = bRoles.stream().map(BRole::getCode).toList();
        boolean containsAtLesatTwo = roleCodes.size() >= 2 && businessCodes.stream().filter(roleCodes::contains).count() >= 2;
        if (containsAtLesatTwo) {
            throw new ServiceException("不能同时分配2种业务角色: 集团客服|二级公司客服|小程序用户");
        }

        Set<RUserRole> rUserRoles = bRoles.stream()
                .map(role -> new RUserRole()
                        .setUserId(bUser.getUserId())
                        .setRoleId(role.getRoleId())
                        .setTenantId(bUser.getTenantId())
                ).collect(Collectors.toSet());
        rUserRoleDao.saveBatch(rUserRoles);

        sysOperateLogService.saveLog(
                bUser.getTenantId(), OperateModuleEnums.USER,
                "分配用户角色【" + bUser.getUserId() + ", " + bUser.getName() + ", " + bUser.getRealname() + "】"
        );

        return true;
    }


    private void checkMobile(String mobile) {
        if (!PhoneUtil.isPhone(mobile)) {
            throw new ServiceException("手机号码不正确");
        }
    }


    private void checkExistedUser(AddUserReq req) {

        boolean existed = new LambdaQueryChainWrapper<>(BUser.class)
                .eq(BUser::getName, req.getName())
                .exists();
        if (existed) {
            throw new ServiceException("用户已经存在");
        }

        existed = new LambdaQueryChainWrapper<>(BUser.class)
                .eq(BUser::getRealname, req.getRealname())
                .eq(BUser::getMobile, req.getMobile())
                .exists();
        if (existed) {
            throw new ServiceException("用户已经存在");
        }
    }


    /**
     * 重置密码
     */
    public Boolean resetPwd(ResetPwdReq req) {

        AuthVerifyUtils.mustAdmin();

        BUser bUser = bUserDao.getById(req.getUserId());
        Assert.notNull(bUser);

        if (AuthVerifyUtils.notSuperAdmin() && ObjUtil.notEqual(UserInfoContextUtils.getCurrentTenantId(), bUser.getTenantId())) {
            throw new ServiceException("非法请求，不允许操作其他租户用户");
        }

        bUser.setPasswd(req.getPasswd())
                .setPasswdModifyTime(LocalDateTime.now());

        bUserDao.updateById(bUser);

        sysOperateLogService.saveLog(
                bUser.getTenantId(), OperateModuleEnums.USER,
                "重置用户密码【" + bUser.getUserId() + ", " + bUser.getName() + ", " + bUser.getRealname() + "】"
        );

        return true;
    }
}
