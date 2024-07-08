package com.kge.energy.crm.wechat.login.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kge.energy.crm.repository.dao.BUserDao;
import com.kge.energy.crm.repository.dao.LUserTokenDao;
import com.kge.energy.crm.repository.dao.RUserRoleDao;
import com.kge.energy.crm.repository.entity.*;
import com.kge.energy.crm.repository.entityext.param.OmReportListParam;
import com.kge.energy.crm.wechat.login.property.WechatProperties;
import com.kge.energy.crm.wechat.login.req.WechatLoginReq;
import com.kge.energy.crm.wechat.login.req.WechatAccessReq;
import com.kge.energy.crm.wechat.login.resp.WechatLoginResp;
import com.kge.energy.crm.wechat.login.resp.WechatAccessResp;
import com.kge.platform.framework.web.util.RestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 用户登录服务层
 * @author zqy
 */
@Service
@RequiredArgsConstructor
public class WechatLoginService {

    private final BUserDao bUserDao;
    private final LUserTokenDao lUserTokenDao;
    private final RUserRoleDao rUserRoleDao;
    private final WechatProperties wechatProperties;

    /**
     * 首页微信登录获取openid
     */
    @Transactional
    public WechatLoginResp login(WechatLoginReq req) {


        //请求微信接口
        WechatAccessResp wechatAccess = getWxAccessToken(req.getJsCode());
        BUser u = bUserDao.FindUserByMobile(wechatAccess.getOpenId());

        if(u.getUserId() == 0) {
            //添加用户
            BUser bUser =  new BUser().setOpenId(wechatAccess.getOpenId()).setFlag(1).setType("社会客户").setMobile(req.getMobile());
            bUserDao.save(bUser);

            RUserRole rRole =  new RUserRole().setRoleId(5).setUserId(bUser.getUserId()).setCreateUserId(bUser.getUserId());
            rUserRoleDao.save(rRole);

        }


         return new WechatLoginResp().setCode(0)
                        .setOpenId(wechatAccess.getOpenId())
                        .setToken("")//genToken()
                        .setCode(0);



    }


    public WechatAccessResp getWxAccessToken(String code) {

        String url = wechatProperties.getWxUrl()+"/sns/jscode2session";
        WechatAccessReq req = new WechatAccessReq()
                .setAppId(wechatProperties.getAppId())
                .setAppSecret(wechatProperties.getAppSecret())
                .setCode(code)
                .setGrantType("authorization_code");

        return RestUtils.postForObject(url, req, WechatAccessResp.class);
    }

    public String genToken(BUser bUser) {
        String authToken = "";

        LUserToken lUserToken =  lUserTokenDao.FindByUid(bUser.getUserId());
        if(lUserToken == null || lUserToken.getUserTokenId()==0 ){
            //新增token
            LUserToken lUserTokenNew =  new LUserToken().setLoginToken(authToken)
                    .setLoginExpiredTime(LocalDateTime.now().plusHours(12));
            lUserTokenDao.save(lUserTokenNew);
        } else {

        }
        return authToken;
    }


//    func genTokenNew(userInfo Model.UserModel) string {
//
//        authToken := Token.GUIDMake()
//        uid := strconv.Itoa(userInfo.UserId)
//        var utm Model.UserTokenModel
//        BaseQuery.QueryModel{
//            Conditions: &Model.UserTokenModel{
//                UserId: userInfo.UserId,
//            },
//            Results: &utm,
//        }.Find()
//        expiredTime := time.Now().Add(time.Hour * 12).Format("2006-01-02 15:04:05")
//        redisFront, _ := Configger.ContentGetString("redis", "tokenfront")
//        Redis.SAdd(redisFront+"tokenset", authToken)
//        Redis.Set(redisFront+authToken, uid, time.Hour*121)
//        //shareManage.DBDateTimeEntity.GetLoginExpiredTime().DBDateTime.Format("2006-01-02 15:04:05")
//        if utm.UserTokenId != 0 {
//            BaseQuery.QueryModel{
//                Conditions: &Model.UserTokenModel{
//                    UserTokenId: utm.UserTokenId,
//                },
//                Data: map[string]interface{}{
//                    "LoginToken":       authToken,
//                            "LoginExpiredTime": expiredTime,
//                },
//            }.Update()
//
//            //	authToken = utm.LoginToken
//        } else {
//
//            BaseQuery.QueryModel{
//                Data: &Model.UserTokenModel{
//                    UserId:           userInfo.UserId,
//                            LoginExpiredTime: expiredTime,
//                            LoginToken:       authToken,
//                },
//            }.Insert()
//
//        }
//
//        return authToken
//    }








}
