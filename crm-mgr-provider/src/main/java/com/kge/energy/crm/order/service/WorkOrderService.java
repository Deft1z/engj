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
import com.kge.energy.crm.workorder.req.WfFormPageReq;
import com.kge.energy.crm.workorder.service.WorkOrderDomainService;
import com.kge.platform.framework.common.exception.ServiceException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    @Value("${excel.export.limit-days:180}")
    private Integer limitDays;

    private final WfFormFlowService wfFormFlowService;

    private final WorkOrderDomainService workOrderDomainService;

    /*
        工单导出
     */
    public void exportWorkOrder(HttpServletResponse response, WorkOrderExportReq req) {
        WorkOrderExportReq.SearchFormMap searchMap = Optional.ofNullable(req.getSearchMap()).orElse(new WorkOrderExportReq.SearchFormMap());
        if (searchMap.getStarttime() != null && searchMap.getEndtime() != null) {
            long days = Duration.between(searchMap.getStarttime(), searchMap.getEndtime()).toDays();
            //限制导出的最大时间范围
            if (days > limitDays) {
                throw new ServiceException(String.format("数据导出时间范围不得超过%s天", limitDays));
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
        ExcelUtils.write(response, "工单列表数据.xls", "工单列表数据", WfFormExportDto.class, exportDtoList, req.getExportType());
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
