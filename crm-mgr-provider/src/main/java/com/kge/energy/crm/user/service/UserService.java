package com.kge.energy.crm.user.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.common.constans.TokenConstant;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.net.ResponseCode;
import com.kge.energy.crm.common.page.PageResp;
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
import com.kge.platform.framework.common.net.CommonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
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

    private final BRoleDao bRoleDao;

    private final SysOperateLogService sysOperateLogService;

    private final TenantDomainService tenantDomainService;

    private final SysLoginLogHandleService sysLoginLogHandleService;

    private final UserDomainService userDomainService;


    public BUser getUserByMobile(String mobile) {
        return bUserDao.getUserByMobile(mobile);
    }

    public List<RoleUserResp> getUserByRoleId(RoleUserReq req) {

        Integer userId = UserInfoContextUtils.getCurrentUserId();

        List<RoleUserResult> roleUserResults;

        if (UserInfoContextUtils.getCurrentUserInfo().getRoleList().stream().anyMatch(role -> ObjectUtil.equals(role.getId(), RoleIdEnums.SYSTEM_ADMINISTRATOR.getCode()))) {
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

        if (ObjectUtil.isNotNull(bUser)) {
            return bUser.getPasswdSalt();
        }

        // 找不到name到系统表中获取盐值，md5(username+salt),确保相同的name获取相同的盐值,防止通过盐值测试用户名是否存在
        SSystemConfig sSystemConfig = sSystemConfigDao.findByName("SALTBASE");

        return MD5.create().digestHex(req.getName() + sSystemConfig.getConfig());
    }

    public CommonResult<UserLoginResp> userLogin(UserLoginReq req) {
        UserLoginResp userLoginResp = null;
        BUser bUser = null;

        try {

            bUser = bUserDao.getOne(Wrappers.lambdaQuery(new BUser().setName(req.getName()).setPasswd(req.getPasswd())));

            if (ObjectUtil.isNull(bUser)) {
                throw new ServiceException(ResponseCode.SHOULD_LOGIN.getCode(), ResponseCode.SHOULD_LOGIN.getMsg());
            }

            // 获取uid关联的租户
            RUserTenant rUserTenant = rUserTenantDao.findTenantByUid(bUser.getUserId());

            String authToken = userDomainService.genToken(bUser, SystemTypeEnum.MGR, TokenConstant.PC_EXPIRED_TIMEOUT, TokenConstant.PC_EXPIRED_TIMEUNIT, true);

            bUser.setLastLoginTime(LocalDateTime.now());
            bUserDao.updateById(bUser);

            userLoginResp = new UserLoginResp()
                    .setUserId(bUser.getUserId())
                    .setTenantId(rUserTenant.getOrganizationId())
                    .setAuthToken(authToken)
                    .setMsg("login success");

            //记录登录成功日志
            sysLoginLogHandleService.saveLoginLog(bUser, LoginPlatformEnums.PC, LoginResultEnums.SUCCESS, null);

            return CommonResult.suc(userLoginResp);

        } catch (Exception e) {
            log.error("pc login error: ", e);

            //记录登录失败日志
            sysLoginLogHandleService.saveLoginLog(bUser, LoginPlatformEnums.PC, LoginResultEnums.FAIL, e.toString());

            return CommonResult.fail(ResponseCode.SHOULD_LOGIN.getCode(), ResponseCode.SHOULD_LOGIN.getMsg(), userLoginResp);
        }

    }

    public CurrentUserInfoResp currentUserInfo() {

        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();

        if (ObjectUtil.isNull(userInfoDto)) {
            throw new ServiceException(ResponseCode.TOKEN_FAIL.getCode(), ResponseCode.TOKEN_FAIL.getMsg());
        }

        if (CollectionUtil.isEmpty(userInfoDto.getOrganizationList())) {
            throw new ServiceException("找不到用户对应的租户ID");
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


    public PageResp<WxUserListResp> findAppletUser(WxUserListReq req) {

        if (AuthVerifyUtils.notSuperAdmin() && ObjectUtil.isNull(req.getTenantId())) {
            req.setTenantId(UserInfoContextUtils.getCurrentTenantId());
        }

        if (AuthVerifyUtils.notSuperAdmin() && ObjectUtil.notEqual(UserInfoContextUtils.getCurrentTenantId(), req.getTenantId())) {
            throw new ServiceException("非法请求，不允许查看其他租户信息");
        }

        Page<BUser> page = new Page<>(req.getCurrentPage(), req.getPageSize());

        IPage<BUser> users = bUserDao.findAppletUser(page, req.getTenantId(), req.getName());

        List<WxUserListResp> list = users.getRecords()
                .stream()
                .map(item -> new WxUserListResp()
                        .setUserId(item.getUserId())
                        .setRealname(item.getRealname())
                        .setMobile(item.getMobile())
                        .setCompany(item.getCompany())
                        .setAddress(item.getAddress())
                        .setRemark(item.getRemark())
                        .setTenantId(item.getTenantId())
                ).toList();

        return new PageResp<WxUserListResp>()
                .setCurrentPage(users.getCurrent())
                .setPageSize(users.getSize())
                .setTotal(users.getTotal())
                .setList(list);
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
                        .setTenantId(user.getTenantId())
                ).toList();

        return new PageResp<UserListResp>()
                .setCurrentPage(usersPage.getCurrent())
                .setTotal(usersPage.getTotal())
                .setPageSize(usersPage.getSize())
                .setList(resps);
    }

    /**
     * 获取角色的用户列表
     */
    public PageResp<UserListResp> listByRole(UserListByRoleReq req) {

        AuthVerifyUtils.mustAdmin();

        if (AuthVerifyUtils.notSuperAdmin() && ObjectUtil.notEqual(UserInfoContextUtils.getCurrentTenantId(), req.getTenantId())) {
            throw new ServiceException("非法请求，不允许查看其他租户用户");
        }

        UserListParam userListParam = BeanUtil.copyProperties(req, UserListParam.class);
        IPage<UserListResult> usersPage = bUserDao.listByRole(userListParam);

        List<UserListResp> resps = usersPage.getRecords()
                .stream()
                .map(user -> new UserListResp()
                        .setUserId(user.getUserId())
                        .setName(user.getName())
                        .setRealname(user.getRealname())
                        .setMobile(user.getMobile())
                        .setStatus(user.getStatus())
                        .setOrganizationName(user.getOrganizationName())
                ).toList();

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

        if (AuthVerifyUtils.notSuperAdmin() && ObjectUtil.notEqual(UserInfoContextUtils.getCurrentTenantId(), bUser.getTenantId())) {
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

        if (AuthVerifyUtils.notSuperAdmin() && ObjectUtil.notEqual(UserInfoContextUtils.getCurrentTenantId(), req.getTenantId())) {
            throw new ServiceException("非法请求，不允许添加其他租户用户");
        }

        checkMobile(req.getMobile());

        checkAddExistedUser(req);

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

        checkUpdateExistedUser(req, bUser);

        if (AuthVerifyUtils.notSuperAdmin() && ObjectUtil.notEqual(UserInfoContextUtils.getCurrentTenantId(), bUser.getTenantId())) {
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
        if (ObjectUtil.notEqual(req.getOrganizationId(), rUserTenant.getOrganizationId())) {
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

        if (AuthVerifyUtils.notSuperAdmin() && ObjectUtil.notEqual(UserInfoContextUtils.getCurrentTenantId(), bUser.getTenantId())) {
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

        if (AuthVerifyUtils.notSuperAdmin() && ObjectUtil.notEqual(UserInfoContextUtils.getCurrentTenantId(), bUser.getTenantId())) {
            throw new ServiceException("非法请求，不允分配其他租户用户角色");
        }

        List<BRole> bRoles = bRoleDao.listByIds(req.getRoleIds());
        boolean isCurrentTenantRole = bRoles.stream()
                .allMatch(brole -> ObjectUtil.equals(bUser.getTenantId(), brole.getTenantId()));
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

        String roleNames = bRoles.stream().map(BRole::getName).collect(Collectors.joining("、"));

        Set<RUserRole> rUserRoles = bRoles.stream()
                .map(role -> new RUserRole()
                        .setUserId(bUser.getUserId())
                        .setRoleId(role.getRoleId())
                        .setTenantId(bUser.getTenantId())
                ).collect(Collectors.toSet());
        rUserRoleDao.saveBatch(rUserRoles);

        sysOperateLogService.saveLog(
                bUser.getTenantId(), OperateModuleEnums.USER,
                "分配用户角色【" + bUser.getUserId() + ", " + bUser.getName() + ", " + bUser.getRealname() +
                        ": " + roleNames + "】"
        );

        return true;
    }


    /**
     * 移除用户角色
     */
    public Boolean removeRole(RemoveUserRoleReq req) {
        AuthVerifyUtils.mustAdmin();

        BUser bUser = bUserDao.getById(req.getUserId());
        Assert.notNull(bUser);

        if (AuthVerifyUtils.notSuperAdmin() && ObjectUtil.notEqual(UserInfoContextUtils.getCurrentTenantId(), bUser.getTenantId())) {
            throw new ServiceException("非法请求，不允许操作其他租户用户角色");
        }

        List<BRole> bRoles = bRoleDao.listByIds(req.getRoleIds());
        boolean isCurrentTenantRole = bRoles.stream()
                .allMatch(brole -> ObjectUtil.equals(bUser.getTenantId(), brole.getTenantId()));
        if (AuthVerifyUtils.notSuperAdmin() && !isCurrentTenantRole) {
            throw new ServiceException("非法请求，不允分配其他租户用户角色");
        }

        String roleNames = bRoles.stream().map(BRole::getName).collect(Collectors.joining("、"));

        rUserRoleDao.removeByUserAndRoleIds(bUser.getUserId(), req.getRoleIds());

        sysOperateLogService.saveLog(
                bUser.getTenantId(), OperateModuleEnums.USER,
                "移除用户角色【" + bUser.getUserId() + ", " + bUser.getName() + ", " + bUser.getRealname() +
                        ": " + roleNames + "】"
        );
        return true;
    }

    private void checkMobile(String mobile) {
        if (!PhoneUtil.isPhone(mobile)) {
            throw new ServiceException("手机号码不正确");
        }
    }


    private void checkAddExistedUser(AddUserReq req) {

        boolean existed = new LambdaQueryChainWrapper<>(BUser.class)
                .eq(BUser::getName, req.getName())
                .exists();
        if (existed) {
            throw new ServiceException("用户已经存在");
        }

        existed = new LambdaQueryChainWrapper<>(BUser.class)
                .eq(BUser::getMobile, req.getMobile())
                .exists();
        if (existed) {
            throw new ServiceException("用户已经存在");
        }
    }


    private void checkUpdateExistedUser(UpdateUserReq req, BUser bUser) {

        boolean existed = new LambdaQueryChainWrapper<>(BUser.class)
                .ne(BUser::getUserId, req.getUserId())
                .eq(BUser::getName, req.getName())
                .exists();
        if (existed) {
            throw new ServiceException("用户已经存在");
        }
//
//        existed = new LambdaQueryChainWrapper<>(BUser.class)
//                .ne(BUser::getUserId, req.getUserId())
//                .eq(BUser::getMobile, req.getMobile())
//                .exists();
//        if (existed) {
//            throw new ServiceException("用户已经存在");
//        }
    }


    /**
     * 重置密码
     */
    public Boolean resetPwd(ResetPwdReq req) {

        AuthVerifyUtils.mustAdmin();

        BUser bUser = bUserDao.getById(req.getUserId());
        Assert.notNull(bUser);

        if (AuthVerifyUtils.notSuperAdmin() && ObjectUtil.notEqual(UserInfoContextUtils.getCurrentTenantId(), bUser.getTenantId())) {
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
