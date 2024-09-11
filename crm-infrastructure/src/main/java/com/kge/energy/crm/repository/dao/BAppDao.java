package com.kge.energy.crm.repository.dao;

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
        List<WxUserAppResult> res = mapper.contractPageByUserIdLoad(wxUserAppParam);
        return res;
    }

    /**
     * 小程序客户 -> 获取绑定应用的选择列表
     */
    public List<AppDetailUserResult> appUnbindingListLoad(Integer userId) {
        List<AppDetailUserResult> res = mapper.appUnbindingListLoad(userId);
        return res;
    }

    /**
     * 绑定管理 -> 获取所有应用列表
     */
    public List<BApp> appList() {
        QueryWrapper<BApp> wrapper = Wrappers.query();
        // 封装分页信息
        List<BApp> res = mapper.selectList(wrapper);
        return res;
    }

    /**
     * 绑定管理 -> 内部绑定列表
     */
    public List<OpenIdModelList> newList(Integer page, Integer limit, Integer appId, String mobile, String name) {

        // OpenIdModelList

        // 获取用户对应openid的列表
        List<OpenIdModelList> resultU = mapper.userOpenId(appId, mobile, name, (page - 1) * limit, limit);
        int nullFlag = 0;
        if (resultU.size() < 1) {
            nullFlag = 1;
        }
        List<Integer> uids = new ArrayList<>();

        resultU.forEach(oml -> {
            uids.add(oml.getUid());
        });

        List<OpenIdModelList> result = mapper.userOpenIdProject(uids);
        if (result.size() < 1) {
            nullFlag = 1;
        }

        List<OpenIdModelList> rawCount = mapper.userOpenIdCount(appId, mobile, name);
        if (rawCount.size() < 1) {
            nullFlag = 1;
        }
        List<OpenIdModelList> users = new ArrayList<>();
        if (nullFlag == 0) {
            users = result;
        }
        return users;
    }

    /**
     * 绑定管理 -> 绑定记录的总数
     */
    public Integer newListCount(Integer appId, String mobile, String name) {

        // 获取绑定记录的数量
        List<OpenIdModelList> rawCount = mapper.userOpenIdCount(appId, mobile, name);

        return rawCount.size();
    }

    /**
     * 绑定管理 -> 获得全部应用记录
     */
    public List<BApp> getApps() {
        QueryWrapper<BApp> wrapper = Wrappers.query();
        // 封装分页信息
        List<BApp> applications = mapper.selectList(wrapper);
        return applications;
    }

    /**
     * 绑定管理 -> 详情列表
     */
    public List<OpenShareModelList> FindBindList(Integer page, Integer limit, String mobile, String name, List<Integer> ids) {
        int nullFlag = 0; // 判断记录和总数是否为0
        // 获取绑定记录的列表
        List<OpenShareModelList> result = mapper.FindBindList(page, mobile, name, ids, (page - 1) * limit, limit);
        // 获取绑定记录的列表（总数）
        List<OpenShareModelList> resultCount = mapper.FindBindListCount(mobile, name, ids);
        if (result.size() < 1 || resultCount.size() < 1) {
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
    public Integer FindBindListCount(String mobile, String name, List<Integer> ids) {
        // 获取绑定记录的列表（总数）
        List<OpenShareModelList> resultCount = mapper.FindBindListCount(mobile, name, ids);
        return resultCount.size();
    }

    /**
     * 绑定管理 -> 查找对应的用户组织关系
     */
    public List<OpenIdModelList> FindByUidAndOid(List<Integer> uids, List<Integer> ids) {
        // 查找对应的用户组织关系
        List<OpenIdModelList> result = mapper.FindByUidAndOid(uids, ids);
        return result;
    }

    /**
     * 绑定管理 -> 增加项目记录
     */
    public void AddPro(Integer openId, Integer proId) {
        ROpenidProject rOpenidProject = new ROpenidProject();
        rOpenidProject.setProjectId(proId);
        rOpenidProject.setOpenId(openId);
        rOpenidProject.setFlag(1);
        rOpenidProjectMapper.insert(rOpenidProject);
    }

    /**
     * 绑定管理 -> 增加项目关联关系记录
     */
    public int AddProAndRelation(Integer openId, Integer appId, String name) {
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
        int insertId = rOpenidProjectMapper.insert(openidProject);
        return insertId;
    }

    /**
     * 绑定管理 -> 删除应用关联记录
     */
    public int Del(Integer openId, Integer projectId) {
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
    public int CancelAndUpdate(Integer openId, Integer uid) {
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
    public int CancelAll(List<Integer> openIds) {
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
    public BUser FindUserByMobile(String mobile) {
        LambdaUpdateWrapper<BUser> wrapper = Wrappers.<BUser>update().lambda()
                .like(BUser::getMobile, mobile);
        List<BUser> users = bUserMapper.selectList(wrapper);
        if (users.size() == 0) {
            return null;
        }
        return users.get(0);
    }

    /**
     * 绑定管理 -> 根据userId查找对应的APPID
     */
    public List<UserBindByMobileResult> FindUserBindByUid(Integer userId) {
        List<UserBindByMobileResult> userBindByMobileResult = bUserMapper.findUserBindByUid(userId);
        return userBindByMobileResult;
    }

    /**
     * 绑定管理 -> 绑定APP
     */
    @Transactional
    public Boolean bindApp(Integer uid, Integer openid) {
        if (uid == UserInfoContextUtils.getCurrentUserId()) {
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

