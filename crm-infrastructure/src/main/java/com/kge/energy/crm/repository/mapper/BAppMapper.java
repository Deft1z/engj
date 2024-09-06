package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.repository.entity.BApp;
import com.kge.energy.crm.repository.entityext.param.AppMgrListParam;
import com.kge.energy.crm.repository.entityext.param.WxUserAppParam;
import com.kge.energy.crm.repository.entityext.result.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 接入应用(BApp)表数据库接口层
 */
public interface BAppMapper extends BaseMapper<BApp> {

    // 含有分页功能
    // IPage<WxUserAppResult> contractPageByUserIdLoad(@Param("reqIpage") IPage<WxUserAppParam> reqIpage, @Param("listParam") WxUserAppParam listParam);

    /**
     * 小程序客户 -> 获取合同列表
     */
    List<WxUserAppResult> contractPageByUserIdLoad(@Param("listParam") WxUserAppParam listParam);

    /**
     * 小程序客户 -> 获取应用绑定列表
     */
    List<AppDetailUserResult> appUnbindingListLoad(@Param("userId") Integer userId);

    /**
     * 绑定管理 -> 获取用户对应openid列表
     */
    List<OpenIdModelList> userOpenId(@Param("appid") Integer appid, @Param("mobile") String mobile, @Param("name") String name, @Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 绑定管理 -> 获取对应的用户关联应用的项目信息
     */
    List<OpenIdModelList> userOpenIdProject(@Param("uids") List<Integer> uids);

    /**
     * 绑定管理 -> 已关联openId的用户数
     */
    List<OpenIdModelList> userOpenIdCount(@Param("appid") Integer appid, @Param("mobile") String mobile, @Param("name") String name);


    /**
     * 绑定管理 -> 获取详情列表
     */
    List<OpenShareModelList> FindBindList(@Param("page") Integer page,@Param("mobile") String mobile, @Param("name") String name, @Param("ids") List<Integer> ids,@Param("offset") Integer offset,@Param("limit") Integer limit);


    /**
     * 绑定管理 -> 获取详情列表(总数)
     */
    List<OpenShareModelList> FindBindListCount(@Param("mobile") String mobile, @Param("name") String name, @Param("ids") List<Integer> ids);

    /**
     * 绑定管理 -> 用户绑定组织关系记录
     */
    List<OpenIdModelList> FindByUidAndOid(@Param("uids") List<Integer> uids, @Param("ids") List<Integer> ids);

    /**
     * @description 小程序我的->获取业务系统列表
     * @author tangchenghui
     * @date 2024/7/29 15:16
    */
    List<AppListResult> getAppListByUserId(@Param("userId") Integer userId);

    /**
     * @description 小程序我的->获取业务系统图标列表
     * @author tangchenghui
     * @date 2024/7/29 15:47
    */
    List<AppAvatarListResult> getAppAvatarList();

    /**
     * @description 后台管理-分页获取应用列表
     * @author tangchenghui
     * @date 2024/8/15 17:38
    */
    IPage<AppMgrListResult> selectAppPage(Page<AppMgrListResult> page, @Param("param") AppMgrListParam param);

    IPage<BindUserResult> getBindUsers(Page<BindUserResult> page, @Param("appId") Integer appId, @Param("mobile") String mobile, @Param("name") String name);

}

