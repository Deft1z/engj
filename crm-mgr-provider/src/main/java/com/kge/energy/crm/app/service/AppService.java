package com.kge.energy.crm.app.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.app.req.AppBindingListReq;
import com.kge.energy.crm.app.req.WxUserAppReq;
import com.kge.energy.crm.app.resp.AppDetailUserResc;
import com.kge.energy.crm.app.resp.WxUserAppResp;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.order.req.WxUserWorkOrderReq;
import com.kge.energy.crm.order.resp.FormResp;
import com.kge.energy.crm.repository.dao.BAppDao;
import com.kge.energy.crm.repository.dao.BUserDao;
import com.kge.energy.crm.repository.entityext.param.WorkOrderListParam;
import com.kge.energy.crm.repository.entityext.param.WxUserAppParam;
import com.kge.energy.crm.repository.entityext.param.WxUserWorkOrderParam;
import com.kge.energy.crm.repository.entityext.result.ContractResult;
import com.kge.energy.crm.repository.entityext.result.FormResult;
import com.kge.energy.crm.repository.entityext.result.WxUserAppResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    public List<AppDetailUserResc> appUnbindingListLoad(AppBindingListReq req) {
        List<AppDetailUserResc> resps = BeanUtil.copyToList(bAppDao.appUnbindingListLoad(req.getUserId()), AppDetailUserResc.class);
        return resps;
    }


}
