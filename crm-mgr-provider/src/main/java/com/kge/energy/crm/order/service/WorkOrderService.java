package com.kge.energy.crm.order.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.order.req.WorkOrderListReq;
import com.kge.energy.crm.order.resp.FormModelResp;
import com.kge.energy.crm.repository.dao.WfFormDao;
import com.kge.energy.crm.repository.entityext.param.WorkOrderListParam;
import com.kge.energy.crm.repository.entityext.result.FormModelResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkOrderService {

    private final WfFormDao wfFormDao;

    /**
     * 工单列表
     */
    public PageResp<FormModelResp> list(WorkOrderListReq req) {

        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();
        Assert.notNull(userInfoDto);

        IPage<WorkOrderListParam> reqIpage = new Page<>(req.getCurrentPage(), req.getPageSize());
        WorkOrderListParam workOrderListParam = BeanUtil.copyProperties(req, WorkOrderListParam.class);

        IPage<FormModelResult> pages = wfFormDao.findList(reqIpage, workOrderListParam, userInfoDto);
        List<FormModelResp> resps = BeanUtil.copyToList(pages.getRecords(), FormModelResp.class);

        return new PageResp<FormModelResp>()
                .setList(resps)
                .setCurrentPage(pages.getCurrent())
                .setPageSize(pages.getSize())
                .setTotal(pages.getTotal());
    }
}
