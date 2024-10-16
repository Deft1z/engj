package com.kge.energy.crm.order.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.ExcelUtils;
import com.kge.energy.crm.flow.service.WfFormFlowService;
import com.kge.energy.crm.order.req.WorkOrderExportReq;
import com.kge.energy.crm.order.req.WxUserWorkOrderReq;
import com.kge.energy.crm.order.resp.FormResp;
import com.kge.energy.crm.repository.entityext.param.WxUserWorkOrderParam;
import com.kge.energy.crm.repository.entityext.result.FormResult;
import com.kge.energy.crm.repository.entityext.result.WfFormExportDto;
import com.kge.energy.crm.workOrder.req.WfFormPageReq;
import com.kge.energy.crm.workOrder.service.WorkOrderDomainService;
import com.kge.platform.framework.common.exception.ServiceException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkOrderService {

    private final WfFormFlowService wfFormFlowService;

    private final WorkOrderDomainService workOrderDomainService;

    /**
     * 工单列表
     */
//    public PageResp<FormResp> list(WorkOrderListReq req) {
//        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();
//        Assert.notNull(userInfoDto);
//
//        //数据权限校验，超级管理员可查询全部租户数据，非超管默认只能查询同一租户下的数据
//        if (AuthVerifyUtils.notSuperAdmin() && ObjUtil.isNull(req.getTenantId())) {
//            req.setTenantId(userInfoDto.getTenantId());
//        }
//        if (AuthVerifyUtils.notSuperAdmin() && ObjUtil.notEqual(userInfoDto.getTenantId(), req.getTenantId())) {
//            throw new ServiceException("非法请求，不允许查看其他租户信息");
//        }
//
//        IPage<WorkOrderListParam> reqIpage = new Page<>(req.getCurrentPage(), req.getPageSize());
//        WorkOrderListParam workOrderListParam = BeanUtil.copyProperties(req, WorkOrderListParam.class);
//
//        IPage<FormResult> pages = wfFormDao.findListForWx(reqIpage, workOrderListParam, userInfoDto);
//        List<FormResp> resps = BeanUtil.copyToList(pages.getRecords(), FormResp.class);
//
//        return new PageResp<FormResp>()
//                .setList(resps)
//                .setCurrentPage(pages.getCurrent())
//                .setPageSize(pages.getSize())
//                .setTotal(pages.getTotal());
//    }
    /*
        工单导出
     */
    public void exportWorkOrder(HttpServletResponse response, WorkOrderExportReq req) throws IOException {
        //允许导出的最大时间范围
        final Integer limitDays = 180;
        WorkOrderExportReq.SearchFormMap searchMap = Optional.ofNullable(req.getSearchMap()).orElse(new WorkOrderExportReq.SearchFormMap());
        if (searchMap.getStarttime() != null && searchMap.getEndtime() != null) {
            long days = Duration.between(searchMap.getStarttime(), searchMap.getEndtime()).toDays();
            if (days > limitDays) {
                throw new ServiceException("数据导出时间范围不得超过180天");
            }
        } else {
            //若未选择时间范围，默认为最近限制天数的时间
            LocalDateTime now = LocalDateTime.now();
            searchMap.setStarttime(now.minusDays(limitDays));
            searchMap.setEndtime(now);
            req.setSearchMap(searchMap);
        }

        WfFormPageReq wfFormPageReq = BeanUtil.copyProperties(req, WfFormPageReq.class);
        List<FormResult> all = workOrderDomainService.findAll(wfFormPageReq);

        //数据转为要导出的dto类
        List<WfFormExportDto> exportDtoList = BeanUtil.copyToList(all, WfFormExportDto.class);

        //ExcelUtils写excel 响应给前端
        ExcelUtils.write(response, "工单列表数据.xlsx", "工单列表数据", WfFormExportDto.class, exportDtoList);
    }


    /**
     * 微信小程序客户 -> 工单
     */
    public PageResp<FormResp> getWxUserOrder(WxUserWorkOrderReq req) {
        IPage<WxUserWorkOrderParam> reqIpage = new Page<>(req.getCurrentPage(), req.getPageSize());
        WxUserWorkOrderParam wxUserWorkOrderParam = BeanUtil.copyProperties(req, WxUserWorkOrderParam.class);
        IPage<FormResult> pages = wfFormFlowService.getWxUserWorkOrder(reqIpage, wxUserWorkOrderParam);
        List<FormResp> resps = BeanUtil.copyToList(pages.getRecords(), FormResp.class);

        return new PageResp<FormResp>()
                .setList(resps)
                .setCurrentPage(pages.getCurrent())
                .setPageSize(pages.getSize())
                .setTotal(pages.getTotal());
    }

}
