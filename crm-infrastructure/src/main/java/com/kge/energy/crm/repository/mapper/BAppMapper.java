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


    /**
     * 小程序客户 -> 获取合同列表
     */
    List<WxUserAppResult> contractPageByUserIdLoad(@Param("listParam") WxUserAppParam listParam);

    /**
     * 小程序客户 -> 获取应用绑定列表
     */
    List<AppDetailUserResult> appUnbindingListLoad(@Param("userId") Integer userId);

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

    /**
     * @description 后台管理-分页获取绑定用户列表
     * @param page
     * @param appId
     * @param mobile
     * @param name
     * @return
     */
    IPage<BindUserResult> getBindUsers(Page<BindUserResult> page, @Param("appId") Integer appId, @Param("mobile") String mobile, @Param("name") String name);

    /**
     * @description 后台管理-分页获取绑定分享用户列表
     * @param page
     * @param mobile
     * @param name
     * @param openidIds
     * @return
     */
    IPage<BindUserResult> getBindShareUsers(Page<BindUserResult> page, @Param("mobile") String mobile, @Param("name") String name, @Param("openidIds") List<Integer> openidIds);

}

