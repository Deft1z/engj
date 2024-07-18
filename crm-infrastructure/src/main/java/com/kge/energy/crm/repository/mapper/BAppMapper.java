package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kge.energy.crm.repository.entity.BApp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kge.energy.crm.repository.entityext.param.WxUserAppParam;
import com.kge.energy.crm.repository.entityext.param.WxUserWorkOrderParam;
import com.kge.energy.crm.repository.entityext.result.AppDetailUserResult;
import com.kge.energy.crm.repository.entityext.result.FormResult;
import com.kge.energy.crm.repository.entityext.result.WxUserAppResult;
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

}

