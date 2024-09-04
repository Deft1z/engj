package com.kge.energy.crm.wechat.login.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.kge.energy.crm.common.constans.TokenConstant;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.execption.BadException;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.LoginPlatformEnums;
import com.kge.energy.crm.enums.LoginResultEnums;
import com.kge.energy.crm.enums.RoleEnums;
import com.kge.energy.crm.enums.SystemTypeEnum;
import com.kge.energy.crm.external.elink.ElinkService;
import com.kge.energy.crm.external.wechat.applet.req.SendSubscribeReq;
import com.kge.energy.crm.external.wechat.applet.resp.GetUserPhoneNumberResp;
import com.kge.energy.crm.external.wechat.applet.resp.LoginResp;
import com.kge.energy.crm.external.wechat.applet.resp.SendSubscribeResp;
import com.kge.energy.crm.external.wechat.applet.service.WeChatAppletInfraService;
import com.kge.energy.crm.login.SysLoginLogHandleService;
import com.kge.energy.crm.repository.dao.*;
import com.kge.energy.crm.repository.entity.*;
import com.kge.energy.crm.user.service.UserDomainService;
import com.kge.energy.crm.wechat.login.req.PhoneNumberReq;
import com.kge.energy.crm.wechat.login.req.SendMessageReq;
import com.kge.energy.crm.wechat.login.req.WeChatLoginReq;
import com.kge.energy.crm.wechat.login.resp.WeChatLoginResp;
import com.kge.energy.crm.wechat.login.resp.WeChatPhoneNumberResp;
import com.kge.energy.crm.wechat.login.resp.WxLoginUserInfoResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
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
    private final ElinkService elinkService;

    private final Environment env;
    private final BOrganizationDao bOrganizationDao;
    private final BRoleDao bRoleDao;
    private final RUserTenantDao rUserTenantDao;

    @Value("${spring.data.redis.front}")
    private String redisFront;

    @Value("${loginAttention.leaderPhones}")
    private String[] leaderPhones;

    @Value("${loginAttention.sendee}")
    private String[] sendee;

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
                throw new BadException(Opt.ofNullable(req.getMobile()).orElse("") + "获取openid失败");
            }
            // 现在接口只返回了  {"session_key":"VI6GJ52tcpCQx9eSpLPZlA==","openid":"ocgqB6988rYAugtnawmR6RE2YavE"}
//        if (ObjUtil.notEqual(appletLoginResp.getErrCode(), LoginResp.SUCCESS_CODE)) {
//            throw new BadException(appletLoginResp.getErrMsg());
//        }

            user = bUserDao.findUserByOpenId(appletLoginResp.getOpenId());

            if (ObjectUtil.isNotNull(user)) {
                //统计每日登录
                String key = user.getUserId() + "_" + LocalDateTimeUtil.format(LocalDate.now(), "yyyy_MM_dd");
                String hashKey = redisFront + "dailyLoginCount";
                stringRedisTemplate.opsForHash().increment(key, hashKey, 1);

            } else {
                // 新用户默认挂靠到南投-未挂靠组织下
                user = saveNewUser(appletLoginResp.getOpenId(), req.getMobile());
            }

            String token = userDomainService.genToken(user, SystemTypeEnum.APPLET, TokenConstant.APPLET_EXPIRED_TIMEOUT, TokenConstant.APPLET_EXPIRED_TIMEUNIT, false);

            //记录登录成功日志
            sysLoginLogHandleService.saveLoginLog(user, LoginPlatformEnums.WECHAT_APPLET, LoginResultEnums.FAIL, null);

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
                .setMobile(mobile)
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


    @Async
    public void sendLeaderOnlineMsg(BUser user) {

        CompletableFuture.runAsync(() -> {
            String activeProfile = env.getProperty("spring.profiles.active");
            if (activeProfile.contains("dev")) {
                return;
            }

            List<String> leaderPhoneList = new ArrayList<>(Arrays.asList(leaderPhones));
            List<String> sendeeList = new ArrayList<>(Arrays.asList(sendee));
            if (CollUtil.isEmpty(leaderPhoneList) || CollUtil.isEmpty(sendeeList)) {
                return;
            }

            if (CollUtil.contains(leaderPhoneList, user.getMobile())) {
                String msg = "领导名字：" + user.getRealname() + "\\n" +
                        "手机号：" + user.getMobile() + "\\n" +
                        "登录时间：" + DateUtil.now() +
                        "请重点关注！！！";

                for (String sendPhone : sendeeList) {
                    String msgContent = elinkService.createElinkPushContent(IdUtil.fastSimpleUUID(), "e能管家小程序领导登录提醒", msg, sendPhone);
                    try {
                        elinkService.pushElinkMsg(msgContent);
                    } catch (Exception e) {
                        log.error("sendLeaderOnlineMsg error: ", e);
                    }
                    ThreadUtil.sleep(1, TimeUnit.SECONDS);
                }
            }
        });

    }

    /**
     * 获取微信小程序用户的手机号码
     */
    @Transactional
    public WeChatPhoneNumberResp phoneNumber(PhoneNumberReq req) {

        GetUserPhoneNumberResp getUserPhoneNumberResp = weChatAppletInfraService.getUserPhoneNumber(req.getCode(), req.getOpenid());
        if (ObjectUtil.isNull(getUserPhoneNumberResp) || ObjectUtil.notEqual(getUserPhoneNumberResp.getErrCode(), GetUserPhoneNumberResp.SUCCESS_CODE)) {
            throw new BadException("获取用户手机号码失败");
        }

        List<BUser> userList = new LambdaQueryChainWrapper<>(BUser.class)
                .eq(BUser::getOpenId, req.getOpenid())
                .list();
        String mobile = getUserPhoneNumberResp.getPhoneInfo().getPhoneNumber();
        BUser bUser;

        if (CollUtil.isEmpty(userList)) {
            throw new BadException("用户不存在");
        }

        // openid 只存在一个用户，且手机号为空
        if (userList.size() == 1 && ObjectUtil.isNull(userList.get(0).getMobile())) {
            bUser = userList.get(0);
            bUser.setMobile(mobile);
            bUserDao.updateById(bUser);

        } else {
            // 匹配 openid、手机号用户
            bUser = userList.stream()
                    .filter(user -> ObjectUtil.equal(user.getMobile(), mobile))
                    .findFirst()
                    .orElse(null);

            // 不存在该 openid、手机号用户则新建用户
            if (ObjectUtil.isNull(bUser)) {
                bUser = saveNewUser(req.getOpenid(), mobile);
            }
        }

        String token = userDomainService.genToken(bUser, SystemTypeEnum.APPLET, TokenConstant.APPLET_EXPIRED_TIMEOUT, TokenConstant.APPLET_EXPIRED_TIMEUNIT, false);

        WeChatPhoneNumberResp.Watermark watermark = new WeChatPhoneNumberResp.Watermark()
                .setTimestamp(getUserPhoneNumberResp.getPhoneInfo().getWatermark().getTimestamp())
                .setAppId(getUserPhoneNumberResp.getPhoneInfo().getWatermark().getAppId());

        WeChatPhoneNumberResp.PhoneInfo phoneInfo = new WeChatPhoneNumberResp.PhoneInfo()
                .setPhoneNumber(getUserPhoneNumberResp.getPhoneInfo().getPhoneNumber())
                .setPurePhoneNumber(getUserPhoneNumberResp.getPhoneInfo().getPurePhoneNumber())
                .setWatermark(watermark)
                .setCountryCode(getUserPhoneNumberResp.getPhoneInfo().getCountryCode());

        return new WeChatPhoneNumberResp()
                .setErrCode(getUserPhoneNumberResp.getErrCode())
                .setErrMsg(getUserPhoneNumberResp.getErrMsg())
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
     * 发送订阅消息
     */
    public Boolean sendMessage(SendMessageReq req) {

        SendSubscribeReq sendSubscribeReq = new SendSubscribeReq()
                .setTemplateId(req.getTemplateId())
                .setPage(req.getPage())
                .setToUserOpenId(req.getToUserOpenId())
                .setData(req.getData())
                .setPage(req.getPage())
                .setMiniprogramState(req.getMiniprogramState());

        SendSubscribeResp sendSubscribeResp = weChatAppletInfraService.sendSubscribe(sendSubscribeReq);

        if (ObjUtil.notEqual(sendSubscribeResp.getErrCode(), SendSubscribeResp.SUCCESS_CODE)) {
            return false;
        }

        return true;
    }
}
