package com.kge.energy.crm.wechat.login.service;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.execption.BadException;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.UserTypeEnums;
import com.kge.energy.crm.external.wechat.applet.req.SendSubscribeReq;
import com.kge.energy.crm.external.wechat.applet.resp.GetUserPhoneNumberResp;
import com.kge.energy.crm.external.wechat.applet.resp.LoginResp;
import com.kge.energy.crm.external.wechat.applet.resp.SendSubscribeResp;
import com.kge.energy.crm.external.wechat.applet.service.WeChatAppletInfraService;
import com.kge.energy.crm.repository.dao.BUserDao;
import com.kge.energy.crm.repository.dao.RUserRoleDao;
import com.kge.energy.crm.repository.entity.BUser;
import com.kge.energy.crm.repository.entity.RUserRole;
import com.kge.energy.crm.user.service.UserDomainService;
import com.kge.energy.crm.wechat.login.req.PhoneNumberReq;
import com.kge.energy.crm.wechat.login.req.SendMessageReq;
import com.kge.energy.crm.wechat.login.req.WeChatLoginReq;
import com.kge.energy.crm.wechat.login.resp.WeChatLoginResp;
import com.kge.energy.crm.wechat.login.resp.WeChatPhoneNumberResp;
import com.kge.energy.crm.wechat.login.resp.WxLoginUserInfoResp;
import com.kge.platform.framework.common.enums.IsDeleteFlagEnums;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.stream.Collectors;


/**
 * 用户登录服务层
 *
 * @author zqy
 */
@Service
@RequiredArgsConstructor
public class WeChatLoginService {

    private final BUserDao bUserDao;
    private final RUserRoleDao rUserRoleDao;
    private final StringRedisTemplate stringRedisTemplate;

    private final UserDomainService userDomainService;
    private final WeChatAppletInfraService weChatAppletInfraService;

    @Value("${spring.data.redis.front}")
    private String redisFront;

    /**
     * 首页微信登录获取openid
     */
    @Transactional
    public WeChatLoginResp login(WeChatLoginReq req) {

        //请求微信接口
        LoginResp appletLoginResp = weChatAppletInfraService.appletLogin(req.getJsCode());
        // 现在接口只返回了  {"session_key":"VI6GJ52tcpCQx9eSpLPZlA==","openid":"ocgqB6988rYAugtnawmR6RE2YavE"}
//        if (ObjUtil.notEqual(appletLoginResp.getErrCode(), LoginResp.SUCCESS_CODE)) {
//            throw new BadException(appletLoginResp.getErrMsg());
//        }

        BUser user = bUserDao.findUserByOpenId(appletLoginResp.getOpenId());

        if (ObjUtil.notEqual(user.getUserId(), 0)) {
            //统计每日登录
            String key = user.getUserId() + "_" + LocalDateTimeUtil.format(LocalDate.now(), "yyyy_MM_dd");
            String hashKey = redisFront + "dailyLoginCount";
            stringRedisTemplate.opsForHash().increment(key, hashKey, 1);

        } else {
            //注册用户
            BUser bUser = new BUser().setOpenId(appletLoginResp.getOpenId()).setFlag(1).setType("社会客户").setMobile(req.getMobile());
            bUserDao.save(bUser);

            RUserRole rRole = new RUserRole().setRoleId(5).setUserId(bUser.getUserId()).setCreateUserId(bUser.getUserId());
            rUserRoleDao.save(rRole);

            user = bUserDao.findUserByOpenId(appletLoginResp.getOpenId());
        }

        String token = userDomainService.genToken(user, false);

        return new WeChatLoginResp()
                .setSessionKey(appletLoginResp.getSessionKey())
                .setUnionId(appletLoginResp.getUnionId())
                .setOpenId(appletLoginResp.getOpenId())
                .setErrMsg(appletLoginResp.getErrMsg())
                .setErrCode(appletLoginResp.getErrCode())
                .setToken(token);
    }


    /**
     * 获取微信小程序用户的手机号码
     */
    @Transactional
    public WeChatPhoneNumberResp phoneNumber(PhoneNumberReq req) {

        GetUserPhoneNumberResp getUserPhoneNumberResp = weChatAppletInfraService.getUserPhoneNumber(req.getCode(), req.getOpenid());
        if (ObjUtil.isNull(getUserPhoneNumberResp) || ObjUtil.notEqual(getUserPhoneNumberResp.getErrCode(), GetUserPhoneNumberResp.SUCCESS_CODE)) {
            throw new BadException("获取用户手机号码失败");
        }

        //更新用户绑定手机号码
        String token = updateMobileByUserId(UserInfoContextUtils.getCurrentUserId(),
                getUserPhoneNumberResp.getPhoneInfo().getPhoneNumber(), req.getOpenid());

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
     * todo: 此段逻辑有点复杂，后续需要优化
     */
    private String updateMobileByUserId(Integer userId, String mobile, String openId) {
        BUser bUser = new LambdaQueryChainWrapper<>(BUser.class)
                .eq(BUser::getMobile, mobile)
                .eq(BUser::getType, UserTypeEnums.SYSTEM_USERS.getDesc()).one();

        if (ObjUtil.isNotNull(bUser) && ObjUtil.notEqual(bUser.getUserId(), 0)) {
            //将系统账号绑定openid
            bUser.setOpenId(openId);
            bUserDao.updateById(bUser);

            //将小程序账号置为-1
            new LambdaUpdateChainWrapper<>(BUser.class)
                    .set(BUser::getFlag, IsDeleteFlagEnums.YES)
                    .eq(BUser::getUserId, userId)
                    .in(BUser::getType, UserTypeEnums.SOCIAL_CUSTOMERS.getDesc(), UserTypeEnums.LEADER.getDesc())
                    .update();

            return userDomainService.genToken(bUser, false);

        } else {

            BUser bUser1 = new LambdaQueryChainWrapper<>(BUser.class)
                    .eq(BUser::getMobile, mobile)
                    .in(BUser::getType, UserTypeEnums.SOCIAL_CUSTOMERS.getDesc(), UserTypeEnums.LEADER.getDesc())
                    .in(BUser::getOpenId, "", null)
                    .one();

            if (ObjUtil.isNotNull(bUser1) && ObjUtil.notEqual(bUser1.getUserId(), 0)) {
                //将新小程序账号置为-1
                new LambdaUpdateChainWrapper<>(BUser.class)
                        .set(BUser::getFlag, IsDeleteFlagEnums.YES)
                        .eq(BUser::getUserId, userId)
                        .in(BUser::getType, UserTypeEnums.SOCIAL_CUSTOMERS.getDesc(), UserTypeEnums.LEADER.getDesc())
                        .notIn(BUser::getOpenId, "", null)
                        .in(BUser::getMobile, null, "")
                        .update();

                new LambdaUpdateChainWrapper<>(BUser.class)
                        .set(BUser::getOpenId, openId)
                        .eq(BUser::getMobile, mobile)
                        .in(BUser::getType, UserTypeEnums.SOCIAL_CUSTOMERS.getDesc(), UserTypeEnums.LEADER.getDesc())
                        .update();

                return userDomainService.genToken(bUser1, false);

            } else {

                new LambdaUpdateChainWrapper<>(BUser.class)
                        .set(BUser::getMobile, mobile)
                        .eq(BUser::getUserId, userId)
                        .update();

                return "";
            }
        }

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
                .setUserId(bUser.getUserId())
                .setUserName(bUser.getName())
                .setType(bUser.getType())
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
