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
import com.kge.energy.crm.repository.entityext.result.UserBindByMobileResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        return BeanUtil.copyToList(bAppDao.contractPageByUserIdLoad(wxUserAppParam), WxUserAppResp.class);
    }

    /**
     * 微信客户小程序 -> 绑定的第三方应用 -> 绑定应用选择列表
     */
    public List<AppDetailUserResp> appUnbindingListLoad(AppBindingListReq req) {
        return BeanUtil.copyToList(bAppDao.appUnbindingListLoad(req.getUserId()), AppDetailUserResp.class);
    }

    /**
     * 微信客户小程序 -> 获取所有第三方应用的列表
     */
    public List<BApp> list() {
        return bAppDao.appList();
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
        //应用关联项目的逻辑或许需进一步梳理，当前未实际应用
        IPage<BindUserResult> bindUsers = bAppDao.getBindUsers(pageParam, appid, mobile, name);
        //重构原go开发团队的代码逻辑，为不增加前端的对接工作量，转换成原来的数据结构返回
        return convertListResp(bindUsers);
    }

    private ListResp convertListResp(IPage<BindUserResult> bindUsers) {
        ListResp resp = new ListResp();
        resp.setTotal(Math.toIntExact(bindUsers.getTotal()));
        List<ListContent> content = new ArrayList<>();
        for (BindUserResult bindUser : bindUsers.getRecords()) {
            ListContent listContent = new ListContent();
            listContent.setUid(bindUser.getUserId());
            listContent.setName(bindUser.getRealname());
            listContent.setMobile(bindUser.getMobile());
            listContent.setApps(convertListApp(bindUser));
            content.add(listContent);
        }
        resp.setContent(content);
        return resp;
    }

    private List<App> convertListApp(BindUserResult bindUser) {
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
        return apps;
    }

    public DetailResp findBindList(Integer page, Integer limit, String mobile, String name, List<Integer> ids) {
        Page<BindUserResult> pageParam = new Page<>(page, limit);
        IPage<BindUserResult> bindShareUsers = bAppDao.getBindShareUsers(pageParam, mobile, name, ids);

        // 重构原go开发团队的代码逻辑，为不增加前端的对接工作量，转换成原来的数据结构返回
        DetailResp resp = new DetailResp();
        resp.setTotal(Math.toIntExact(bindShareUsers.getTotal()));

        List<DetailC> content = new ArrayList<>();
        for (BindUserResult bindShareUser : bindShareUsers.getRecords()) {
            DetailC detailC = new DetailC();
            detailC.setUid(bindShareUser.getUserId());
            detailC.setName(bindShareUser.getRealname());
            detailC.setMobile(bindShareUser.getMobile());
            detailC.setApps(convertListApp(bindShareUser));
            content.add(detailC);
        }
        resp.setContent(content);

        return resp;
    }

    public int addProject(AddProReq req) {
        int pid = 0;
        if (req.getProjectid() != null) {
            pid = req.getProjectid();
        }
        if (req.getProjectid() != null) {
            //只需要新建关系
            bAppDao.addPro(req.getOpenid(), req.getProjectid());
        } else if (req.getName().length() > 0) {
            //需要新建关系以及项目名称
            pid = bAppDao.addProAndRelation(req.getOpenid(), req.getAppid(), req.getName());
        } else {
            return 0;
        }
        return pid;
    }

    public int del(ProjectDelReq req) {
        return bAppDao.del(req.getOpenid(), req.getProjectid());
    }

    public int cancelAndUpdate(BindReq req) {
        return bAppDao.cancelAndUpdate(req.getOpenid(), req.getUserid());
    }

    public int cancelAll(List<Integer> openIds) {
        return bAppDao.cancelAll(openIds);
    }


    public UserResp findUserResp(String mobile) {
        BUser bUser = bAppDao.findUserByMobile(mobile);
        UserResp userResp = new UserResp();
        List<UserBindByMobileResult> userBindByMobileResults = new ArrayList<>();
        if (bUser != null) {
            userBindByMobileResults = bAppDao.findUserBindByUid(bUser.getUserId());
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
        return bAppDao.bindApp(req.getUserid(), req.getOpenid());
    }

}
