package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kge.energy.crm.repository.entityext.param.WxUserAppParam;
import com.kge.energy.crm.repository.entityext.param.WxUserWorkOrderParam;
import com.kge.energy.crm.repository.entityext.result.AppDetailUserResult;
import com.kge.energy.crm.repository.entityext.result.FormResult;
import com.kge.energy.crm.repository.entityext.result.WxUserAppResult;
import com.kge.energy.crm.repository.mapper.BAppMapper;
import com.kge.energy.crm.repository.entity.BApp;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * 接入应用(BApp)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class BAppDao extends ServiceImpl<BAppMapper, BApp> {

    private final BAppMapper mapper;

    /**
     * 小程序客户 -> 获取工单
     */
    public List<WxUserAppResult> contractPageByUserIdLoad(WxUserAppParam wxUserAppParam) {
        List<WxUserAppResult> res = mapper.contractPageByUserIdLoad(wxUserAppParam);
        return res;
    }
    /**
     *  含有分页功能呢
     public IPage<WxUserAppResult> contractPageByUserIdLoad(IPage<WxUserAppParam> reqIpage,  WxUserAppParam wxUserAppParam) {
        IPage<WxUserAppResult> res = mapper.contractPageByUserIdLoad(reqIpage,wxUserAppParam);
        return res;
     }
     */

    /**
     * 小程序客户 -> 获取绑定应用的选择列表
     */
    public List<AppDetailUserResult> appUnbindingListLoad(Integer userId) {
        List<AppDetailUserResult> res = mapper.appUnbindingListLoad(userId);
        return res;
    }
}

