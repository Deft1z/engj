package com.kge.energy.crm.app.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.kge.energy.crm.app.req.*;
import com.kge.energy.crm.app.resp.*;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.execption.BadException;
import com.kge.energy.crm.common.net.ResponseCode;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.repository.dao.BAppDao;
import com.kge.energy.crm.repository.entity.BApp;
import com.kge.energy.crm.repository.entity.BUser;
import com.kge.energy.crm.repository.entityext.param.WxUserAppParam;
import com.kge.energy.crm.repository.entityext.result.ContractResult;
import com.kge.energy.crm.repository.entityext.result.OpenIdModelList;
import com.kge.energy.crm.repository.entityext.result.OpenShareModelList;
import com.kge.energy.crm.repository.entityext.result.UserBindByMobileResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

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
    /**  含有分页功能
     public PageResp<WxUserAppResp> contractPageByUserIdLoad(WxUserAppReq req) {
     UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();
     Assert.notNull(userInfoDto);
     if (req.getPageSize() == null || req.getCurrentPage() == null) {
     req.setPageSize(1L);
     req.setCurrentPage(10L);
     }
     IPage<WxUserAppParam> reqIpage = new Page<>(req.getCurrentPage(), req.getPageSize());
     WxUserAppParam wxUserAppParam = BeanUtil.copyProperties(req, WxUserAppParam.class);
     IPage<WxUserAppResult> pages = bAppDao.contractPageByUserIdLoad(reqIpage,wxUserAppParam);
     List<WxUserAppResp> resps = BeanUtil.copyToList(pages.getRecords(), WxUserAppResp.class);
     return new PageResp<WxUserAppResp>()
     .setList(resps)
     .setCurrentPage(pages.getCurrent())
     .setPageSize(pages.getSize())
     .setTotal(pages.getTotal());
     }
     */

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
    public ListResp listNew(Integer page, Integer limit,  Integer appid, String mobile, String name) {
        List<OpenIdModelList> users = bAppDao.newList(page, limit, appid, mobile, name);
        Integer total = bAppDao.newListCount(appid, mobile, name);
        if (users.size() == 0) {
            total = 0;
        }
        List<BApp> apps = bAppDao.getApps();
        Map<Integer, Integer> recordMap = new HashMap<>();
        for (int k = 0; k < apps.size(); k++) {
            recordMap.put(apps.get(k).getAppId(), k + 1);
        }

        List<ListContent> result = new ArrayList<>();
        for (OpenIdModelList userK : users) {
            Integer projectToAppid = userK.getProaid();
            if (userK.getFlag() == 1 && userK.getState() == 1 && recordMap.get(userK.getAppid()) > 0) {
                if (result.size() == 0 || !Objects.equals(userK.getUid(), result.get(result.size() - 1).getUid())) {

                    Project p1 = new Project();
                    p1.setId(userK.getPid());
                    p1.setName(userK.getPname());

                    List<App> appsNull = new ArrayList<>();
                    ListContent c1 = new ListContent();
                    c1.setUid(userK.getUid());
                    c1.setName(userK.getRealname());
                    c1.setMobile(userK.getMobile());
                    c1.setApps(appsNull);

                    App a1 = new App();
                    List<Project> pnull = new ArrayList<>();

                    System.out.println("recordMap = "+recordMap);
                    System.out.println("userK.getAppid() = "+ (userK.getAppid()));
                    System.out.println("recordMap.get(userK.getAppid()) = "+ recordMap.get(userK.getAppid()));
                    System.out.println("apps.get(recordMap.get(userK.getAppid())) = "+ recordMap.get(userK.getAppid()));
                    a1.setName(apps.get(recordMap.get(userK.getAppid()) - 1).getName());
                    a1.setId(userK.getAppid());
                    a1.setOid(userK.getOid());
                    a1.setProjects(pnull);

                    if (p1.getId() != null && userK.getRflag() != -1) {
                        List<Project> projs = new ArrayList<>();
                        projs.add(p1);
                        a1.setProjects(projs);
                    }
                    List<App> appList = new ArrayList<>();
                    appList.add(a1);
                    c1.setApps(appList);
                    result.add(c1);
                    if (userK.getAppid() == 1) {
                        List<App> tmp = result.get(result.size() - 1).getApps();
                        App a2 = new App();
                        a2.setName(apps.get(recordMap.get(2) - 1).getName());
                        a2.setId(2);
                        List<Project> pnull2 = new ArrayList<>();
                        a2.setProjects(pnull2);
                        a2.setOid(userK.getOid());
                        tmp.add(a2);
                        result.get(result.size()-1).setApps(tmp);
                    }
                } else {
                    //同个用户
                    List<App> tmp = result.get(result.size() - 1).getApps();
                    boolean find = false;
                    for (App app : tmp) {
                        if (Objects.equals(app.getId(), projectToAppid)) {
                            if (userK.getPid() != 0 && userK.getRflag() != -1) {
                                List<Project> tmp1 = app.getProjects();
                                Project project = new Project();
                                project.setId(userK.getPid());
                                project.setName(userK.getPname());
                                tmp1.add(project);
                                app.setProjects(tmp1);
                            }
                            find = true;
                        }
                    }
                    if (!find) {
                        App tmpApp = new App();
                        tmpApp.setName(apps.get(recordMap.get(userK.getAppid()) - 1).getName());
                        tmpApp.setId(userK.getAppid());
                        tmpApp.setOid(userK.getOid());
                        if (userK.getPid() != null && userK.getRflag() != -1) {
                            List<Project> prjs = new ArrayList<>();
                            Project pj = new Project();
                            pj.setId(userK.getPid());
                            pj.setName(userK.getPname());
                            prjs.add(pj);
                            tmpApp.setProjects(prjs);
                        }
                        tmp.add(tmpApp);
                    }

                    result.get(result.size() - 1).setApps(tmp);
                }
            }
        }
        ListResp resp = new ListResp();
        resp.setContent(result);
        resp.setTotal(total);
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

        List<OpenIdModelList> oms = bAppDao.FindByUidAndOid(uids,ids);
        Map<Integer, List<Integer>> omMap = new HashMap<>();

        for (OpenIdModelList omsk : oms){
            if (omMap.get(omsk.getUid()) == null ){
//                omMap.get(omsk.getUid()).set(0,omsk.getAppid());
                List<Integer> appIdTemp = new ArrayList<>();
                appIdTemp.add(omsk.getAppid());
                if (omsk.getAppid() == 1){
                    appIdTemp.add(2);
                }
                omMap.put(omsk.getUid(),appIdTemp);
            }else{
                List<Integer> tmp = omMap.get(omsk.getUid());
                tmp.add(omsk.getAppid());
                if (omsk.getAppid() == 1){
                    tmp.add(2);
                }
                omMap.put(omsk.getUid(),tmp);
            }
        }
        System.out.println("omMap = " + omMap);
        List<DetailC> result = new ArrayList<>();
        for (OpenShareModelList usersk : users) {
            DetailC content = new DetailC();
            content.setUid(usersk.getUid());
            content.setName(usersk.getRealname());
            content.setMobile(usersk.getMobile());
            List<App> appsNew = new ArrayList<>();
            for (Integer m : omMap.get(usersk.getUid())){
                App appTemp = new App();
                appTemp.setName(apps.get(m-1).getName());
                appTemp.setId(apps.get(m-1).getAppId());
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

    public int addProject(AddProReq req){
        int pid = 0;
        if (req.getProjectid() != null) {
            pid = req.getProjectid();
        }
        if (req.getProjectid() != null ){
            //只需要新建关系
            bAppDao.AddPro(req.getOpenid(),req.getProjectid());
        }else if (req.getName().length() > 0){
            //需要新建关系以及项目名称
            pid = bAppDao.AddProAndRelation(req.getOpenid(), req.getAppid(), req.getName());
        }else{
            return 0;
        }
        return pid;
    }

    public int Del(ProjectDelReq req){
        return bAppDao.Del(req.getOpenid(), req.getProjectid());
    }

    public int CancelAndUpdate(BindReq req){
        return bAppDao.CancelAndUpdate(req.getOpenid(), req.getUserid());
    }

    public int CancelAll(List<Integer> openIds){
        return bAppDao.CancelAll(openIds);
    }


    public UserResp FindUserResp(String mobile){
        BUser bUser = bAppDao.FindUserByMobile(mobile);
        UserResp userResp = new UserResp();
        List<UserBindByMobileResult> userBindByMobileResults = new ArrayList<>();
        if (bUser != null) {
            userBindByMobileResults = bAppDao.FindUserBindByUid(bUser.getUserId());
            userResp.setUserid(bUser.getUserId());
            userResp.setName(bUser.getRealname());
            for (UserBindByMobileResult ubmr : userBindByMobileResults){
                List<Integer> tmp = new ArrayList<>();
                tmp.add(ubmr.getAppid());
                if (ubmr.getAppid() == 1){
                    tmp.add(2);
                }
                userResp.setAppids(tmp);

            }
        }
        return userResp;
    }

    public Boolean bindApp(BindReq req){
        Boolean result = bAppDao.bindApp(req.getUserid(),req.getOpenid());
        return result;
    }

}
