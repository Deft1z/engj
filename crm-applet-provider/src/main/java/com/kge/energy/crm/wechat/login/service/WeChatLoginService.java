package com.kge.energy.crm.wechat.login.service;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjUtil;
import com.kge.energy.crm.common.execption.BadException;
import com.kge.energy.crm.external.wechat.resp.WeChatAppletLoginResp;
import com.kge.energy.crm.external.wechat.service.WeChatInfraService;
import com.kge.energy.crm.repository.dao.BUserDao;
import com.kge.energy.crm.repository.dao.RUserRoleDao;
import com.kge.energy.crm.repository.entity.BUser;
import com.kge.energy.crm.repository.entity.RUserRole;
import com.kge.energy.crm.user.service.UserDomainService;
import com.kge.energy.crm.wechat.login.req.WeChatLoginReq;
import com.kge.energy.crm.wechat.login.resp.WeChatLoginResp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


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
    private final WeChatInfraService weChatInfraService;

    @Value("${spring.data.redis.front}")
    private String redisFront;

    /**
     * 首页微信登录获取openid
     */
    @Transactional
    public WeChatLoginResp login(WeChatLoginReq req) {

        //请求微信接口
        WeChatAppletLoginResp appletLoginResp = weChatInfraService.appletLogin(req.getJsCode());
        if (ObjUtil.notEqual(appletLoginResp.getErrCode(), 0)) {
            throw new BadException(appletLoginResp.getErrMsg());
        }

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


}
