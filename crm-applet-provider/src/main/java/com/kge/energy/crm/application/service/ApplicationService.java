package com.kge.energy.crm.application.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kge.energy.crm.application.req.AppBindReq;
import com.kge.energy.crm.application.req.AppDetailReq;
import com.kge.energy.crm.application.req.AppTokenReq;
import com.kge.energy.crm.application.req.AppUnbindReq;
import com.kge.energy.crm.application.resp.AppDetailResp;
import com.kge.energy.crm.application.resp.AppTokenResp;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.execption.BadException;
import com.kge.energy.crm.common.net.CommonResponse;
import com.kge.energy.crm.common.net.ResponseCode;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.external.ct.req.CtTokenReq;
import com.kge.energy.crm.external.ct.service.CtService;
import com.kge.energy.crm.repository.dao.BAppDao;
import com.kge.energy.crm.repository.dao.BOpenidDao;
import com.kge.energy.crm.repository.dao.BOpenidShareDao;
import com.kge.energy.crm.repository.entity.BApp;
import com.kge.energy.crm.repository.entity.BOpenid;
import com.kge.energy.crm.repository.entityext.result.AppAvatarListResult;
import com.kge.energy.crm.repository.entityext.result.AppListResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final BAppDao appDao;

    private final BOpenidDao openidDao;

    private final BOpenidShareDao openidShareDao;

    private final CtService ctService;

    public List<AppListResult> getAppList() {
        List<AppListResult> appListResultList = appDao.getAppListByUserId(UserInfoContextUtils.getCurrentUserId());
        Map<Integer, Integer> appMap = new HashMap<>();
        for (int k = 0; k < appListResultList.size(); k++) {
            AppListResult current = appListResultList.get(k);
            appMap.put(current.getAppId(), k);

            if (current.getAppId() == 1) {
                AppListResult apps2 = new AppListResult(2, "智能电房监测", current.getBindingTime());
                appListResultList.add(apps2);
                appMap.put(2, appListResultList.size() - 1);
            }
        }

        List<AppAvatarListResult> appAvatarListResultList = appDao.getAppAvatarList();
        for (AppAvatarListResult a : appAvatarListResultList) {
            if (appMap.containsKey(a.getAppId())) {
                int key = appMap.get(a.getAppId());
                appListResultList.get(key).setFilepath(a.getFilepath());
            }
        }

        return appListResultList;
    }

    public boolean unbindApp(AppUnbindReq appUnbindReq) {
        BOpenid openid = openidDao.getOpenId(UserInfoContextUtils.getCurrentUserId(), appUnbindReq.getAppId());

        if (ObjectUtil.isNull(openid)) {
            throw new BadException(7, "当前账号未绑定该业务系统", "messagebox");
        }

        if (openid.getBindingState() != 1) {
            throw new BadException(7, "当前账号未绑定该业务系统", "messagebox");
        }

        int result = openidDao.logicDeleteOpenId(openid.getOpenidId());
        if (result != 1) {
            return false;
        }
        return true;
    }

    public CommonResponse<Object> getAppToken(AppTokenReq appTokenReq) throws NoSuchAlgorithmException, JsonProcessingException {
        AppTokenResp appTokenResp = new AppTokenResp();

        // 2024-04-19临时解决多对多问题
        if (appTokenReq.getAppId() == 2) {
            appTokenReq.setAppId(1);
        }

        Integer userId = UserInfoContextUtils.getCurrentUserId();

        // 获取当前用户openId
        BOpenid bOpenid = openidDao.getOpenId(userId, appTokenReq.getAppId());
        Integer shareOpenId = 0;
        if (ObjectUtil.isNull(bOpenid)) {
            shareOpenId = openidShareDao.getShareBind(userId, appTokenReq.getAppId());
            if (shareOpenId == 0) {
                BOpenid newOpenId = new BOpenid().setUserId(userId)
                        .setAppId(appTokenReq.getAppId())
                        .setBindingState(0)
                        .setFlag(1);
                openidDao.save(newOpenId);
                appTokenResp.setOpenId(newOpenId.getOpenidId().toString());
                return new CommonResponse<>(ResponseCode.SUC.getCode(), "当前账号未绑定该业务系统，请先进行绑定", "messagebox", appTokenResp);
            }
        } else {
            shareOpenId = bOpenid.getOpenidId();
            if (NumberUtil.equals(bOpenid.getBindingState(), Integer.valueOf(0))) {
                appTokenResp.setOpenId(bOpenid.getOpenidId().toString());
                return new CommonResponse<>(ResponseCode.SUC.getCode(), "当前账号未绑定该业务系统，请先进行绑定", "messagebox", appTokenResp);
            }
        }

        // 获取第三方业务系统地址
        BApp bApp = appDao.getById(appTokenReq.getAppId());
        CtTokenReq ctTokenReq = BeanUtil.copyProperties(bApp, CtTokenReq.class);
        ctTokenReq.setOpenid(shareOpenId);
        JSONObject jsonObject = ctService.getCtToken(ctTokenReq);

        if (!jsonObject.containsKey("ret")) {
            log.error("响应未找到ret字段,{}", jsonObject.toString());
            return CommonResponse.suc("用户不存在");
        }
        String ret = jsonObject.getStr("ret");
        if (StrUtil.equals("4005", ret)) {
            log.error("用户未绑定,{}", jsonObject.getStr("msg"));
            bOpenid.setBindingState(0);
            openidDao.updateById(bOpenid);
            appTokenResp.setOpenId(bOpenid.getOpenidId().toString());
            return new CommonResponse<>(ResponseCode.SUC.getCode(), "当前账号未绑定该业务系统，请先进行绑定", "messagebox", appTokenResp);
        } else {
            if (!StrUtil.equals("0", ret)) {
                return CommonResponse.suc(jsonObject.getStr("msg"));
            }
        }

        if (!jsonObject.containsKey("data")) {
            log.error("响应未找到data字段,{}", jsonObject.toString());
            return CommonResponse.suc("用户不存在");
        }
        JSONObject data = jsonObject.getJSONObject("data");
        if (!data.containsKey("token")) {
            log.error("响应未找到token字段,{}", jsonObject.toString());
            throw new BadException("应用出现错误");
        }

        appTokenResp = JSONUtil.toBean(data, AppTokenResp.class);
        return CommonResponse.suc(appTokenResp);
    }

    public Boolean bindApp(AppBindReq appBindReq) {
        if (appBindReq.getAppId() == 2) {
            appBindReq.setAppId(1);
        }

        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();
        Integer userId = userInfoDto.getUserId().intValue();
        String mobile = userInfoDto.getMobile();

        // 获取当前用户openId
        BOpenid bOpenid = openidDao.getOpenId(userId, appBindReq.getAppId());
        if (ObjectUtil.isNull(bOpenid)) {
            // 未绑定
            //如果有提前绑定过内部的多对一，那就无需多生成一条新的绑定记录
            Integer shareOpenId = openidShareDao.getShareBind(userId, appBindReq.getAppId());
            if (NumberUtil.equals(shareOpenId, Integer.valueOf(0))) {
                BOpenid newOpenId = new BOpenid();
                newOpenId.setUserId(userId);
                newOpenId.setAppId(appBindReq.getAppId());
                newOpenId.setExternalAccount(mobile);
                newOpenId.setBindingState(1);
                newOpenId.setBindingTime(LocalDateTime.now());
                newOpenId.setFlag(1);
                return openidDao.save(newOpenId);
            }
        } else {
            // 绑定状态不为1
            if (NumberUtil.equals(bOpenid.getBindingState(), Integer.valueOf(0))) {
                bOpenid.setExternalAccount(mobile);
                bOpenid.setBindingState(1);
                bOpenid.setBindingTime(LocalDateTime.now());
                return openidDao.updateById(bOpenid);
            }
        }
        return true;
    }

    /**
     * 获取APP详情信息
     */
    public List<AppDetailResp> getAppDetail(AppDetailReq appTokenReq) {

        List<BApp> apps = new LambdaQueryChainWrapper<>(BApp.class)
                .in(BApp::getAppId, appTokenReq.getAppIds())
                .list();

        return apps.stream()
                .map(app -> new AppDetailResp()
                        .setAppId(app.getAppId())
                        .setName(app.getName())
                        .setAppAddress(app.getAppAddress())
                        .setBindAddress(app.getBindAddress())
                ).collect(Collectors.toList());
    }
}
