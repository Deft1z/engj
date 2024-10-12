package com.kge.energy.crm.order.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.page.PageResp;
import com.kge.energy.crm.common.util.ExcelUtils;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.BizFunctionEnums;
import com.kge.energy.crm.enums.DataPermissionRangeTypeEnums;
import com.kge.energy.crm.flow.service.WfFormFlowService;
import com.kge.energy.crm.order.req.WorkOrderExportReq;
import com.kge.energy.crm.order.req.WxUserWorkOrderReq;
import com.kge.energy.crm.order.resp.FormResp;
import com.kge.energy.crm.permission.service.DataPermissionDomainService;
import com.kge.energy.crm.repository.dao.ScServiceContractDao;
import com.kge.energy.crm.repository.dao.WfFormDao;
import com.kge.energy.crm.repository.entityext.param.WorkOrderListParam;
import com.kge.energy.crm.repository.entityext.param.WxUserWorkOrderParam;
import com.kge.energy.crm.repository.entityext.result.ContractResult;
import com.kge.energy.crm.repository.entityext.result.FormResult;
import com.kge.energy.crm.repository.entityext.result.WfFormExportDto;
import com.kge.energy.crm.workOrder.resp.WfFormPageResp;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkOrderService {

    private final WfFormFlowService wfFormFlowService;

    private final WfFormDao wfFormDao;

    private final ScServiceContractDao scServiceContractDao;

    private final DataPermissionDomainService dataPermissionDomainService;

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
    public Boolean exportWorkOrder(HttpServletResponse response, WorkOrderExportReq req) throws IOException {
        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();
        Assert.notNull(userInfoDto);
        //获取筛选条件下的工单列表 (复用分页查询 size设为INT_MAX)
        IPage<WorkOrderListParam> reqIpage = new Page<>(req.getCurrentPage(), Integer.MAX_VALUE);
        WorkOrderListParam workOrderListParam = BeanUtil.copyProperties(req, WorkOrderListParam.class);
        DataPermissionRangeTypeEnums dataEnums = dataPermissionDomainService.getCurrentUserDataPermission(BizFunctionEnums.BIZORDER_LIST);
        IPage<FormResult> pages = wfFormDao.findListForWx(reqIpage, workOrderListParam, userInfoDto,dataEnums);
        List<WfFormPageResp> formList = BeanUtil.copyToList(pages.getRecords(), WfFormPageResp.class);

        //通过工单id获取工单对应的所有合同
        Map<Integer, List<ContractResult>> formIdtoContractList = new HashMap<>();
        formList.forEach(form ->{
            List<ContractResult> contractResults = scServiceContractDao.form(form.getFormId(),userInfoDto,dataEnums);
            contractResults.forEach(contractResult -> {
                formIdtoContractList.computeIfAbsent(contractResult.getFormId(), k -> new ArrayList<>());
                formIdtoContractList.get(contractResult.getFormId()).add(contractResult);
            });
        });

        //数据转为要导出的dto类
        List<WfFormExportDto> exportDtoList = new ArrayList<>();
        formList.forEach(form -> {
            //补充dto类中合同相关字段
            List<ContractResult> contracts = formIdtoContractList.get(form.getFormId());
            if(ObjUtil.isNotEmpty(contracts)){
                //工单有合同
                contracts.forEach(contract -> {
                    WfFormExportDto wfFormExportDto = BeanUtil.copyProperties(form, WfFormExportDto.class);
                    wfFormExportDto.setIfContractSigned("已签合同");
                    wfFormExportDto.setContractName(contract.getName());
                    wfFormExportDto.setContractSignTime(contract.getSigningTime());
                    wfFormExportDto.setContractAmount(contract.getAmount());
                    exportDtoList.add(wfFormExportDto);
                });
            }else{
                //工单无合同
                WfFormExportDto wfFormExportDto = BeanUtil.copyProperties(form, WfFormExportDto.class);
                wfFormExportDto.setIfContractSigned("未签合同");
                exportDtoList.add(wfFormExportDto);
            }
        });
        //ExcelUtils写excel 响应给前端
        ExcelUtils.write(response,"工单列表数据.xlsx","工单列表数据",WfFormExportDto.class,exportDtoList);
//        if(ObjUtil.isNotEmpty(exportDtoList)){
//            LocalDateTime now = LocalDateTime.now();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
//            String formattedDateTime = now.format(formatter);
//            String fileName = System.getProperty("user.home") + File.separator +
//                    "Desktop" + File.separator + formattedDateTime + "WorkOrderListExport.xlsx";
//            log.error(fileName);
//            ExcelUtils.write(response,fileName,"导出工单列表数据",WfFormExportDto.class,exportDtoList);
//            EasyExcel.write(fileName, WfFormExportDto.class)
//                    .sheet("导出工单列表数据")
//                    .doWrite(exportDtoList);
//        }
        return true;
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
