package com.kge.energy.crm.wechat.login.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kge.energy.crm.common.constans.TokenConstant;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.*;
import com.kge.energy.crm.external.wechat.applet.resp.GetUserPhoneNumberResp;
import com.kge.energy.crm.external.wechat.applet.resp.LoginResp;
import com.kge.energy.crm.external.wechat.applet.service.WeChatAppletInfraService;
import com.kge.energy.crm.log.service.SysOperateLogHandleService;
import com.kge.energy.crm.login.SysLoginLogHandleService;
import com.kge.energy.crm.msg.MsgDomainService;
import com.kge.energy.crm.repository.dao.*;
import com.kge.energy.crm.repository.entity.*;
import com.kge.energy.crm.user.service.UserDomainService;
import com.kge.energy.crm.wechat.login.req.GetRecommendQrCodeReq;
import com.kge.energy.crm.wechat.login.req.PhoneNumberReq;
import com.kge.energy.crm.wechat.login.req.WeChatLoginReq;
import com.kge.energy.crm.wechat.login.resp.WeChatLoginResp;
import com.kge.energy.crm.wechat.login.resp.WeChatPhoneNumberResp;
import com.kge.energy.crm.wechat.login.resp.WxAppletRecommendQrCodeResp;
import com.kge.energy.crm.wechat.login.resp.WxLoginUserInfoResp;
import com.kge.energy.msg.dto.UserContactDto;
import com.kge.energy.msg.param.LeaderLoginMsgToRoleParam;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


/**
 * 用户登录服务层
 *
 * @author zqy
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatLoginService {

    private final BUserDao bUserDao;
    private final RUserRoleDao rUserRoleDao;
    private final StringRedisTemplate stringRedisTemplate;

    private final UserDomainService userDomainService;
    private final WeChatAppletInfraService weChatAppletInfraService;
    private final SysLoginLogHandleService sysLoginLogHandleService;
    private final SysOperateLogHandleService sysOperateLogHandleService;

    private final BOrganizationDao bOrganizationDao;
    private final BRoleDao bRoleDao;
    private final RUserTenantDao rUserTenantDao;

    private final MsgDomainService msgDomainService;

    @Value("${spring.data.redis.front}")
    private String redisFront;

    @Value("${loginAttention.sendee}")
    private String[] sendee;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    /**
     * 首页微信登录获取openid
     */
    @Transactional
    public WeChatLoginResp login(WeChatLoginReq req) {
        BUser user = null;

        try {
            //请求微信接口
            LoginResp appletLoginResp = weChatAppletInfraService.appletLogin(req.getJsCode());
            if (StrUtil.isBlank(appletLoginResp.getOpenId())) {
                throw new ServiceException(Opt.ofNullable(req.getMobile()).orElse("") + "获取openid失败");
            }
            // 现在接口只返回了  {"session_key":"VI6GJ52tcpCQx9eSpLPZlA==","openid":"ocgqB6988rYAugtnawmR6RE2YavE"}
//        if (ObjUtil.notEqual(appletLoginResp.getErrCode(), LoginResp.SUCCESS_CODE)) {
//            throw new ServiceException(appletLoginResp.getErrMsg());
//        }

            //根据openid查找小程序用户
            List<BUser> bUsers = bUserDao.findUserByOpenId(appletLoginResp.getOpenId());

            if (CollectionUtil.isNotEmpty(bUsers)) {
                user = bUsers.stream().filter(bUser -> ObjectUtil.equal(bUser.getMobile(), req.getMobile()))
                        .findFirst()
                        .orElse(bUsers.get(0));
                //判断是否禁用
                if (ObjectUtil.equal(user.getStatus(), UserStatusEnums.FORBIDDEN.getCode())) {
                    throw new ServiceException("账号已禁用");
                }

            } else {
                // 新用户默认挂靠到南投-未挂靠组织下
                user = saveNewUser(appletLoginResp.getOpenId(), req.getMobile());
                // 新用户的推荐用户字段绑定
                if (ObjectUtil.isNotNull(req.getRecommendUserId()))
                    user.setRecommendUserId(req.getRecommendUserId());
            }

            String token = userDomainService.genToken(user, SystemTypeEnum.APPLET, TokenConstant.APPLET_EXPIRED_TIMEOUT,
                    TokenConstant.APPLET_EXPIRED_TIMEUNIT, false);

            user.setLastLoginTime(LocalDateTime.now());
            bUserDao.updateById(user);

            //统计每日登录
            String key = user.getUserId() + "_" + LocalDateTimeUtil.format(LocalDate.now(), "yyyy_MM_dd");
            String hashKey = redisFront + "dailyLoginCount";
            stringRedisTemplate.opsForHash().increment(key, hashKey, 1);

            //记录登录成功日志
            sysLoginLogHandleService.saveLoginLog(user, LoginPlatformEnums.WECHAT_APPLET, LoginResultEnums.SUCCESS, null);

            //发送领导登录通知
            sendLeaderOnlineMsg(user);

            return new WeChatLoginResp()
                    .setSessionKey(appletLoginResp.getSessionKey())
                    .setUnionId(appletLoginResp.getUnionId())
                    .setOpenId(appletLoginResp.getOpenId())
                    .setErrMsg(appletLoginResp.getErrMsg())
                    .setErrCode(appletLoginResp.getErrCode())
                    .setToken(token);
        } catch (Exception e) {
            //记录登录失败日志
            sysLoginLogHandleService.saveLoginLog(user, LoginPlatformEnums.WECHAT_APPLET, LoginResultEnums.FAIL, e.getMessage());
            throw new RuntimeException(e);
        }

    }

    /**
     * 新用户默认挂靠到南投-未挂靠组织下,没有未挂靠组织就挂到南投下
     */
    private BUser saveNewUser(String openId, String mobile) {

        Integer defaultTenantId = 1;

        BUser bUser = new BUser()
                .setOpenId(openId)
                .setMobile(StrUtil.isNotBlank(mobile) ? mobile : null)
                .setStatus(0)
                .setFlag(1)
                .setTenantId(defaultTenantId);
        bUserDao.save(bUser);

        BRole bRole = bRoleDao.getTenantRoleByCode(defaultTenantId, RoleEnums.APPLET_USER.getCode());
        RUserRole rRole = new RUserRole()
                .setRoleId(bRole.getRoleId())
                .setUserId(bUser.getUserId())
                .setCreateUserId(bUser.getUserId())
                .setTenantId(bUser.getTenantId());
        rUserRoleDao.save(rRole);

        BOrganization bOrganization = bOrganizationDao.findByTenantOrgName(defaultTenantId, "未挂靠组织");
        bOrganization = ObjectUtil.isNull(bOrganization) ? bOrganizationDao.getRootOrgList(defaultTenantId).get(0) : bOrganization;

        RUserTenant rUserTenant = new RUserTenant()
                .setUserId(bUser.getUserId())
                .setOrganizationId(bOrganization.getOrganizationId())
                .setTenantId(bUser.getTenantId())
                .setFlag(1);
        rUserTenantDao.save(rUserTenant);

        return bUser;
    }


    public void sendLeaderOnlineMsg(BUser user) {

        CompletableFuture.runAsync(() -> {
            if (activeProfile.contains("dev")) {
                return;
            }

            List<String> sendeeList = new ArrayList<>(Arrays.asList(sendee));
            if (CollUtil.isEmpty(sendeeList)) {
                return;
            }

            //查找用户角色
            List<BRole> roleList = bRoleDao.userRole(user.getTenantId(), user.getUserId());
            List<String> roleCodeList = roleList.stream().map(BRole::getCode).toList();

            //如果是集团领导或公司领导则发送登录提醒
            if (CollUtil.contains(roleCodeList, RoleEnums.JT_LEADER.getCode()) ||
                    CollUtil.contains(roleCodeList, RoleEnums.COMPANY_LEADER.getCode())) {
                List<BUser> notifyUsers = bUserDao.list(Wrappers.<BUser>lambdaQuery().in(BUser::getMobile, sendeeList));
                msgDomainService.sendCrmMsg(new LeaderLoginMsgToRoleParam()
                        .setRealname(user.getRealname())
                        .setMobile(user.getMobile())
                        .setLoginTime(DateUtil.now())
                        .setTenantId(user.getTenantId())
                        .setNotifyUsers(BeanUtil.copyToList(notifyUsers, UserContactDto.class))
                );
            }
        });

    }

    /**
     * 获取微信小程序用户的手机号码
     */
    @Transactional
    public WeChatPhoneNumberResp phoneNumber(PhoneNumberReq req) {

        GetUserPhoneNumberResp phoneNumberResp = weChatAppletInfraService.getUserPhoneNumber(req.getCode(), req.getOpenid());
        if (ObjectUtil.isNull(phoneNumberResp) || ObjectUtil.notEqual(phoneNumberResp.getErrCode(), GetUserPhoneNumberResp.SUCCESS_CODE)) {
            throw new ServiceException("获取用户手机号码失败");
        }

        List<BUser> userList = bUserDao.findUserByOpenId(req.getOpenid());
        if (CollUtil.isEmpty(userList)) {
            throw new ServiceException("用户不存在");
        }

        String mobile = phoneNumberResp.getPhoneInfo().getPhoneNumber();
        BUser bUser;

        // 匹配 openid + 手机号用户
        bUser = userList.stream()
                .filter(user -> ObjectUtil.equal(user.getMobile(), mobile))
                .findFirst()
                .orElse(null);

        if (ObjectUtil.isNull(bUser)) {
            List<BUser> byPhoneUserList = bUserDao.findByPhone(mobile);

            if (CollectionUtil.isEmpty(byPhoneUserList) && userList.size() == 1 && StrUtil.isBlank(userList.get(0).getMobile())) {
                // openid 只存在一个用户，且不存在该手机号用户
                bUser = userList.get(0);
                bUser.setMobile(mobile);

            } else if (userList.size() == 1 && byPhoneUserList.size() == 1) {
                // 先有PC手机号码的账号，再登录小程序，要把小程序的有openid且没有手机号码的账号删掉，且将PC手机号码的账号设置openid
                bUser = byPhoneUserList.get(0).setOpenId(req.getOpenid());
                bUserDao.removeById(userList.get(0));

            } else {
                // 不存在该 openid + 手机号用户则新建用户
                bUser = saveNewUser(req.getOpenid(), mobile);
            }
        }

        String token = userDomainService.genToken(bUser, SystemTypeEnum.APPLET, TokenConstant.APPLET_EXPIRED_TIMEOUT,
                TokenConstant.APPLET_EXPIRED_TIMEUNIT, false);

        bUser.setLastLoginTime(LocalDateTime.now());
        bUserDao.updateById(bUser);

        WeChatPhoneNumberResp.Watermark watermark = new WeChatPhoneNumberResp.Watermark()
                .setTimestamp(phoneNumberResp.getPhoneInfo().getWatermark().getTimestamp())
                .setAppId(phoneNumberResp.getPhoneInfo().getWatermark().getAppId());

        WeChatPhoneNumberResp.PhoneInfo phoneInfo = new WeChatPhoneNumberResp.PhoneInfo()
                .setPhoneNumber(phoneNumberResp.getPhoneInfo().getPhoneNumber())
                .setPurePhoneNumber(phoneNumberResp.getPhoneInfo().getPurePhoneNumber())
                .setWatermark(watermark)
                .setCountryCode(phoneNumberResp.getPhoneInfo().getCountryCode());

        return new WeChatPhoneNumberResp()
                .setErrCode(phoneNumberResp.getErrCode())
                .setErrMsg(phoneNumberResp.getErrMsg())
                .setPhoneInfo(phoneInfo)
                .setToken(token);
    }


    /**
     * 获取登陆用户信息
     */
    public WxLoginUserInfoResp getWxLoginUserInfo() {

        UserInfoDto currentUserInfo = UserInfoContextUtils.getCurrentUserInfo();

        BUser bUser = bUserDao.getById(currentUserInfo.getUserId());

        if (ObjUtil.isNull(bUser)) {
            return null;
        }
        return new WxLoginUserInfoResp()
                .setTenantId(currentUserInfo.getTenantId())
                .setTenantName(currentUserInfo.getTenantName())
                .setUserId(bUser.getUserId())
                .setUserName(bUser.getName())
                .setMobile(bUser.getMobile())
                .setRealname(bUser.getRealname())
                .setCompany(bUser.getCompany())
                .setAddress(bUser.getAddress())
                .setRoleList(currentUserInfo.getRoleList()
                        .stream()
                        .map(role -> new WxLoginUserInfoResp.Role()
                                .setId(role.getId())
                                .setName(role.getName())
                                .setCode(role.getCode())
                        ).collect(Collectors.toList()))
                .setOrganizationList(currentUserInfo.getOrganizationList()
                        .stream()
                        .map(org -> new WxLoginUserInfoResp.Organization()
                                .setId(org.getId())
                                .setName(org.getName())
                        ).collect(Collectors.toList()));
    }

    /**
     * 获取小程序url
     */
    public WxAppletRecommendQrCodeResp getWxAppletRecommendQrCode(GetRecommendQrCodeReq req) {
        UserInfoDto currentUserInfo = UserInfoContextUtils.getCurrentUserInfo();

        String page = "pages/index/index";
        String scene = "recommendUserId=" + currentUserInfo.getUserId().toString();

        String base64Str = weChatAppletInfraService.getUnlimitedQRCode(page,scene,req.getWidth());

        sysOperateLogHandleService.saveLog(currentUserInfo.getTenantId(), OperateModuleEnums.USER,
                "生成个人推荐二维码【" + currentUserInfo.getUserId() + " , " + currentUserInfo.getRealname() +"】"
        );

        return new WxAppletRecommendQrCodeResp()
                .setRecommendQrCodeBase64(base64Str);
    }
}
