package com.kge.energy.crm.app.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.app.req.*;
import com.kge.energy.crm.app.resp.*;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.repository.dao.BAppDao;
import com.kge.energy.crm.repository.entity.BApp;
import com.kge.energy.crm.repository.entity.BUser;
import com.kge.energy.crm.repository.entityext.param.WxUserAppParam;
import com.kge.energy.crm.repository.entityext.result.BindUserResult;
import com.kge.energy.crm.repository.entityext.result.OpenIdModelList;
import com.kge.energy.crm.repository.entityext.result.OpenShareModelList;
import com.kge.energy.crm.repository.entityext.result.UserBindByMobileResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangrongjun
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppService {

    private final BAppDao bAppDao;

    /**
     * 微信客户小程序 -> 绑定的第三方应用
     */
    public List<WxUserAppResp> contractPageByUserIdLoad(WxUserAppReq req) {
        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();
        Assert.notNull(userInfoDto);
        WxUserAppParam wxUserAppParam = BeanUtil.copyProperties(req, WxUserAppParam.class);
        List<WxUserAppResp> resps = BeanUtil.copyToList(bAppDao.contractPageByUserIdLoad(wxUserAppParam), WxUserAppResp.class);
        return resps;
    }

    /**
     * 微信客户小程序 -> 绑定的第三方应用 -> 绑定应用选择列表
     */
    public List<AppDetailUserResp> appUnbindingListLoad(AppBindingListReq req) {
        List<AppDetailUserResp> resps = BeanUtil.copyToList(bAppDao.appUnbindingListLoad(req.getUserId()), AppDetailUserResp.class);
        return resps;
    }

    /**
     * 微信客户小程序 -> 获取所有第三方应用的列表
     */
    public List<BApp> list() {
        List<BApp> resps = bAppDao.appList();
        return resps;
    }

    /**
     * 微信客户小程序 -> 绑定的第三方应用 -> 绑定应用选择列表
     */
    public ListResp listNew(Integer page, Integer limit, Integer appid, String mobile, String name) {
        //重构原go开发团队的代码逻辑
        //原go开发团队写死了业务逻辑，当已绑定光伏appid=1时，同时返回电房appid=2的记录，实际数据库关联表并无appid=2的关联记录，所以可认为当查询appid=2时即是查appid=1
        if (appid != null && appid.equals(2)) {
            appid = 1;
        }
        Page<BindUserResult> pageParam = new Page<>(page, limit);
        //todo 应用关联项目的逻辑需进一步梳理
        IPage<BindUserResult> bindUsers = bAppDao.getBindUsers(pageParam, appid, mobile, name);
        //重构原go开发团队的代码逻辑，为不增加前端的对接工作量，使用原来的数据结构返回
        ListResp resp = new ListResp();
        resp.setTotal(Math.toIntExact(bindUsers.getTotal()));
        List<ListContent> content = new ArrayList<>();
        for (BindUserResult bindUser : bindUsers.getRecords()) {
            ListContent listContent = new ListContent();
            listContent.setUid(bindUser.getUserId());
            listContent.setName(bindUser.getRealname());
            listContent.setMobile(bindUser.getMobile());
            List<App> apps = new ArrayList<>();
            if (!bindUser.getRelateApp().isEmpty()) {
                for (BindUserResult.RelateApp relateApp : bindUser.getRelateApp()) {
                    App app = new App();
                    app.setId(relateApp.getAppId());
                    app.setName(relateApp.getAppName());
                    app.setOid(relateApp.getOpenidId());
                    List<Project> projects = new ArrayList<>();
                    if (!relateApp.getRelateProject().isEmpty()) {
                        for (BindUserResult.RelateProject relateProject : relateApp.getRelateProject()) {
                            Project project = new Project();
                            project.setId(relateProject.getProjectId());
                            project.setName(relateProject.getProjectName());
                            projects.add(project);
                        }
                    }
                    app.setProjects(projects);
                    apps.add(app);
                }
            }
            listContent.setApps(apps);
            content.add(listContent);
        }
        resp.setContent(content);

        return resp;
    }

    public DetailResp FindBindList(Integer page, Integer limit, String mobile, String name, List<Integer> ids) {
        List<OpenShareModelList> users = bAppDao.FindBindList(page, limit, mobile, name, ids);
        Integer resultCount = bAppDao.FindBindListCount(mobile, name, ids);
        List<Integer> uids = new ArrayList<>();
        for (OpenShareModelList osml : users) {
            uids.add(osml.getUid());
        }
        List<BApp> apps = bAppDao.getApps();
        Map<Integer, Integer> recordMap = new HashMap<>();
        for (int k = 0; k < apps.size(); k++) {
            recordMap.put(apps.get(k).getAppId(), k + 1);
        }

        List<OpenIdModelList> oms = bAppDao.FindByUidAndOid(uids, ids);
        Map<Integer, List<Integer>> omMap = new HashMap<>();

        for (OpenIdModelList omsk : oms) {
            if (omMap.get(omsk.getUid()) == null) {
//                omMap.get(omsk.getUid()).set(0,omsk.getAppid());
                List<Integer> appIdTemp = new ArrayList<>();
                appIdTemp.add(omsk.getAppid());
                if (omsk.getAppid() == 1) {
                    appIdTemp.add(2);
                }
                omMap.put(omsk.getUid(), appIdTemp);
            } else {
                List<Integer> tmp = omMap.get(omsk.getUid());
                tmp.add(omsk.getAppid());
                if (omsk.getAppid() == 1) {
                    tmp.add(2);
                }
                omMap.put(omsk.getUid(), tmp);
            }
        }
        List<DetailC> result = new ArrayList<>();
        for (OpenShareModelList usersk : users) {
            DetailC content = new DetailC();
            content.setUid(usersk.getUid());
            content.setName(usersk.getRealname());
            content.setMobile(usersk.getMobile());
            List<App> appsNew = new ArrayList<>();
            for (Integer m : omMap.get(usersk.getUid())) {
                App appTemp = new App();
                appTemp.setName(apps.get(m - 1).getName());
                appTemp.setId(apps.get(m - 1).getAppId());
                appsNew.add(appTemp);
            }
            content.setApps(appsNew);
            result.add(content);
        }
        DetailResp resp = new DetailResp();
        resp.setTotal(resultCount);
        resp.setContent(result);
        return resp;
    }

    public int addProject(AddProReq req) {
        int pid = 0;
        if (req.getProjectid() != null) {
            pid = req.getProjectid();
        }
        if (req.getProjectid() != null) {
            //只需要新建关系
            bAppDao.AddPro(req.getOpenid(), req.getProjectid());
        } else if (req.getName().length() > 0) {
            //需要新建关系以及项目名称
            pid = bAppDao.AddProAndRelation(req.getOpenid(), req.getAppid(), req.getName());
        } else {
            return 0;
        }
        return pid;
    }

    public int Del(ProjectDelReq req) {
        return bAppDao.Del(req.getOpenid(), req.getProjectid());
    }

    public int CancelAndUpdate(BindReq req) {
        return bAppDao.CancelAndUpdate(req.getOpenid(), req.getUserid());
    }

    public int CancelAll(List<Integer> openIds) {
        return bAppDao.CancelAll(openIds);
    }


    public UserResp FindUserResp(String mobile) {
        BUser bUser = bAppDao.FindUserByMobile(mobile);
        UserResp userResp = new UserResp();
        List<UserBindByMobileResult> userBindByMobileResults = new ArrayList<>();
        if (bUser != null) {
            userBindByMobileResults = bAppDao.FindUserBindByUid(bUser.getUserId());
            userResp.setUserid(bUser.getUserId());
            userResp.setName(bUser.getRealname());
            for (UserBindByMobileResult ubmr : userBindByMobileResults) {
                List<Integer> tmp = new ArrayList<>();
                tmp.add(ubmr.getAppid());
                if (ubmr.getAppid() == 1) {
                    tmp.add(2);
                }
                userResp.setAppids(tmp);

            }
        }
        return userResp;
    }

    public Boolean bindApp(BindReq req) {
        Boolean result = bAppDao.bindApp(req.getUserid(), req.getOpenid());
        return result;
    }

}
