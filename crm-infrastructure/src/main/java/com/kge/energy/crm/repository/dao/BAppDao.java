package com.kge.energy.crm.repository.dao;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.repository.entity.*;
import com.kge.energy.crm.repository.entityext.param.AppMgrListParam;
import com.kge.energy.crm.repository.entityext.param.WxUserAppParam;
import com.kge.energy.crm.repository.entityext.result.*;
import com.kge.energy.crm.repository.mapper.*;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 接入应用(BApp)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class BAppDao extends ServiceImpl<BAppMapper, BApp> {

    private final BAppMapper mapper;

    private final BProjectMapper bProjectMappermapper;

    private final ROpenidProjectMapper rOpenidProjectMapper;

    private final BOpenidShareMapper bOpenidShareMapper;

    private final BUserMapper bUserMapper;

    private final BOpenidMapper bOpenidMapper;


    /**
     * 小程序客户 -> 获取工单
     */
    public List<WxUserAppResult> contractPageByUserIdLoad(WxUserAppParam wxUserAppParam) {
        return mapper.contractPageByUserIdLoad(wxUserAppParam);
    }

    /**
     * 小程序客户 -> 获取绑定应用的选择列表
     */
    public List<AppDetailUserResult> appUnbindingListLoad(Integer userId) {
        return mapper.appUnbindingListLoad(userId);
    }

    /**
     * 绑定管理 -> 获取所有应用列表
     */
    public List<BApp> appList() {
        QueryWrapper<BApp> wrapper = Wrappers.query();
        // 封装分页信息
        return mapper.selectList(wrapper);
    }

    /**
     * 绑定管理 -> 获得全部应用记录
     */
    public List<BApp> getApps() {
        QueryWrapper<BApp> wrapper = Wrappers.query();
        return mapper.selectList(wrapper);
    }

    /**
     * 绑定管理 -> 详情列表
     */
    public List<OpenShareModelList> findBindList(Integer page, Integer limit, String mobile, String name, List<Integer> ids) {
        int nullFlag = 0; // 判断记录和总数是否为0
        // 获取绑定记录的列表
        List<OpenShareModelList> result = mapper.findBindList(page, mobile, name, ids, (page - 1) * limit, limit);
        // 获取绑定记录的列表（总数）
        List<OpenShareModelList> resultCount = mapper.findBindListCount(mobile, name, ids);
        if (CollectionUtil.isEmpty(result) || CollectionUtil.isEmpty(resultCount)) {
            nullFlag = 1;
        }
        List<OpenShareModelList> openShareModelList = new ArrayList<>();
        if (nullFlag == 0) {
            openShareModelList = result;
        }
        return openShareModelList;
    }


    /**
     * 绑定管理 -> 详情列表(总数)
     */
    public Integer findBindListCount(String mobile, String name, List<Integer> ids) {
        // 获取绑定记录的列表（总数）
        List<OpenShareModelList> resultCount = mapper.findBindListCount(mobile, name, ids);
        return resultCount.size();
    }

    /**
     * 绑定管理 -> 查找对应的用户组织关系
     */
    public List<OpenIdModelList> findByUidAndOid(List<Integer> uids, List<Integer> ids) {
        return mapper.findByUidAndOid(uids, ids);
    }

    /**
     * 绑定管理 -> 增加项目记录
     */
    public void addPro(Integer openId, Integer proId) {
        ROpenidProject rOpenidProject = new ROpenidProject();
        rOpenidProject.setProjectId(proId);
        rOpenidProject.setOpenId(openId);
        rOpenidProject.setFlag(1);
        rOpenidProjectMapper.insert(rOpenidProject);
    }

    /**
     * 绑定管理 -> 增加项目关联关系记录
     */
    public int addProAndRelation(Integer openId, Integer appId, String name) {
        BProject bProject = new BProject();
        bProject.setAppId(appId);
        bProject.setName(name);
        bProject.setFlag(1);
        int e = bProjectMappermapper.insert(bProject);

        if (e == 0) {
            return 0;
        }
        ROpenidProject openidProject = new ROpenidProject();
        openidProject.setProjectId(e);
        openidProject.setOpenId(openId);
        openidProject.setFlag(1);
        return rOpenidProjectMapper.insert(openidProject);
    }

    /**
     * 绑定管理 -> 删除应用关联记录
     */
    public int del(Integer openId, Integer projectId) {
        LambdaUpdateWrapper<ROpenidProject> wrapper = Wrappers.<ROpenidProject>update().lambda()
                .set(ROpenidProject::getFlag, -1)
                .eq(ROpenidProject::getOpenId, openId)
                .eq(ROpenidProject::getProjectId, projectId);
        int resultInt = rOpenidProjectMapper.update(wrapper);
        if (resultInt == 0) {
            return 0;
        }
        return 1;
    }

    /**
     * 绑定管理 -> 删除该用户关联的某个应用
     */
    public int cancelAndUpdate(Integer openId, Integer uid) {
        LambdaUpdateWrapper<BOpenidShare> wrapper = Wrappers.<BOpenidShare>update().lambda()
                .set(BOpenidShare::getFlag, -1)
                .eq(BOpenidShare::getShareOpenidId, openId)
                .eq(BOpenidShare::getUserId, uid);
        int resultInt = bOpenidShareMapper.update(wrapper);
        if (resultInt == 0) {
            return 0;
        }
        return 1;
    }

    /**
     * 绑定管理 -> 删除所有用户关联的某个应用
     */
    public int cancelAll(List<Integer> openIds) {
        LambdaUpdateWrapper<BOpenidShare> wrapper = Wrappers.<BOpenidShare>update().lambda()
                .set(BOpenidShare::getFlag, -1)
                .in(BOpenidShare::getShareOpenidId, openIds);
        int resultInt = bOpenidShareMapper.update(wrapper);
        if (resultInt == 0) {
            return 0;
        }
        return 1;
    }

    /**
     * 绑定管理 -> 根据手机号码查找用户
     */
    public BUser findUserByMobile(String mobile) {
        LambdaUpdateWrapper<BUser> wrapper = Wrappers.<BUser>update().lambda()
                .like(BUser::getMobile, mobile);
        List<BUser> users = bUserMapper.selectList(wrapper);
        if (CollectionUtil.isEmpty(users)) {
            return null;
        }
        return users.get(0);
    }

    /**
     * 绑定管理 -> 根据userId查找对应的APPID
     */
    public List<UserBindByMobileResult> findUserBindByUid(Integer userId) {
        return bUserMapper.findUserBindByUid(userId);
    }

    /**
     * 绑定管理 -> 绑定APP
     */
    @Transactional
    public Boolean bindApp(Integer uid, Integer openid) {
        if (ObjectUtil.equals(uid, UserInfoContextUtils.getCurrentUserId())) {
            throw new ServiceException("非当前用户");
        }
        LambdaUpdateWrapper<BOpenid> wrapper = Wrappers.<BOpenid>update().lambda()
                .set(BOpenid::getFlag, -1)
                .eq(BOpenid::getUserId, uid);
        bOpenidMapper.update(wrapper);

        BOpenidShare bOpenidShare = new BOpenidShare();
        bOpenidShare.setUserId(uid);
        bOpenidShare.setShareOpenidId(openid);
        bOpenidShare.setFlag(1);
        bOpenidShareMapper.insert(bOpenidShare);

        return true;
    }

    /**
     * @description 小程序我的->获取业务系统列表
     * @author tangchenghui
     * @date 2024/7/29 15:28
     */
    public List<AppListResult> getAppListByUserId(Integer userId) {
        return mapper.getAppListByUserId(userId);
    }

    public List<AppAvatarListResult> getAppAvatarList() {
        return mapper.getAppAvatarList();
    }

    public IPage<AppMgrListResult> selectAppPage(AppMgrListParam param) {
        Page<AppMgrListResult> page = new Page<>(param.getCurrentPage(), param.getPageSize());
        return mapper.selectAppPage(page, param);
    }

    public Long getCountByName(String name) {
        LambdaQueryWrapper<BApp> wrapper = Wrappers.<BApp>lambdaQuery().eq(BApp::getName, name);
        return mapper.selectCount(wrapper);
    }

    public Long getOtherCountByIdAndName(Integer appId, String name) {
        LambdaQueryWrapper<BApp> wrapper = Wrappers.<BApp>lambdaQuery().eq(BApp::getName, name).ne(BApp::getAppId, appId);
        return mapper.selectCount(wrapper);
    }

    public IPage<BindUserResult> getBindUsers(Page<BindUserResult> page, Integer appId, String mobile, String name) {
        return mapper.getBindUsers(page, appId, mobile, name);
    }
}

