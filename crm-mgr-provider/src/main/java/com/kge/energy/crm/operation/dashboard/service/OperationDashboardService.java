package com.kge.energy.crm.operation.dashboard.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.enums.WriteDirectionEnum;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.kge.energy.crm.common.util.ExcelUtils;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.OrgTypeEnum;
import com.kge.energy.crm.operation.dashboard.req.OperationDashboardReq;
import com.kge.energy.crm.operation.data.resp.OperationDataOrgResp;
import com.kge.energy.crm.operation.data.service.OperationDataDomainService;
import com.kge.energy.crm.repository.dao.BOrganizationDao;
import com.kge.energy.crm.repository.entity.BOrganization;
import com.kge.energy.crm.repository.entityext.param.StatisticalDataParam;
import com.kge.energy.crm.repository.entityext.result.*;
import com.kge.platform.framework.common.exception.ServiceException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author wangjihua
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class OperationDashboardService {

    private final OperationDataDomainService operationDataDomainService;

    private final BOrganizationDao bOrganizationDao;

    /**
     * 公司筛选列表
     */
    public List<OperationDataOrgResp> orgList() {
        return operationDataDomainService.orgList();
    }

    /**
     * 客户、工单、合同、投诉统计
     */
    public StatisticalDataResult statisticalData(OperationDashboardReq req) {
        return operationDataDomainService.statisticalData(defaultStatisticalDataParam(req));
    }


    /**
     * 新用户增长曲线数据
     */
    public DashboardStatResult newUserGrowthData(OperationDashboardReq req) {
        //参数校验
        checkAndSetParams(req);
        return operationDataDomainService.newUserGrowthData(defaultStatisticalDataParam(req));
    }

    public DashboardStatResult orderContractQtyData(OperationDashboardReq req) {
        //参数校验
        checkAndSetParams(req);
        return operationDataDomainService.orderContractQtyData(defaultStatisticalDataParam(req));
    }

    public DashboardStatResult orderContractAmountData(OperationDashboardReq req) {
        //参数校验
        checkAndSetParams(req);
        return operationDataDomainService.orderContractAmountData(defaultStatisticalDataParam(req));
    }

    public DashboardStatResult complainPctData(OperationDashboardReq req) {
        return operationDataDomainService.complainPctData(defaultStatisticalDataParam(req));
    }

    public DashboardStatResult complainQtyData(OperationDashboardReq req) {
        //参数校验
        checkAndSetParams(req);
        return operationDataDomainService.complainQtyData(defaultStatisticalDataParam(req));
    }

    private void checkAndSetParams(OperationDashboardReq req) {
        LocalDate startDate = req.getStartTime();
        LocalDate endDate = req.getEndTime();
        String dimension = req.getDimension();
        long duration = Duration.between(startDate.atTime(LocalTime.MIN), endDate.atTime(LocalTime.MAX)).toDays();
        if (dimension.equals("week")) {
            if (duration > 84) {
                throw new ServiceException("时间查询范围跨度不能超过12周");
            }
        } else {
            if (duration > 366) {
                throw new ServiceException("时间查询范围跨度不能超过12个月");
            }
        }
        List<String> statDims = new ArrayList<>();
        genStatDims(statDims, startDate, endDate, dimension);
        req.setStatDims(statDims);
    }

    private List<String> genStatDims(List<String> statDims, LocalDate startTime, LocalDate endTime, String dimension) {
        int year = startTime.getYear();
        if (dimension.equals("week")) {
            WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 1);
            int week = startTime.get(weekFields.weekOfYear());
            statDims.add(year + CharSequenceUtil.padPre(String.valueOf(week), 2, "0"));
            startTime = startTime.plusDays(7);
            if (startTime.isBefore(endTime) || startTime.isEqual(endTime)) {
                genStatDims(statDims, startTime, endTime, dimension);
            }
        } else {
            int month = startTime.getMonthValue();
            statDims.add(year + CharSequenceUtil.padPre(String.valueOf(month), 2, "0"));
            startTime = startTime.plusMonths(1);
            if (startTime.isBefore(endTime) || startTime.isEqual(endTime)) {
                genStatDims(statDims, startTime, endTime, dimension);
            }
        }
        return statDims;
    }


    private StatisticalDataParam defaultStatisticalDataParam(OperationDashboardReq req) {
        LocalDate startTime = Optional.ofNullable(req.getStartTime()).orElse(LocalDate.now().withDayOfMonth(1));
        LocalDate endTime = Optional.ofNullable(req.getEndTime()).orElse(LocalDate.now());

        Integer orgId = req.getOrgId();
        BOrganization bOrganization = bOrganizationDao.getById(req.getOrgId());
        // 选择集团则看所有业务公司总数据
        if (ObjectUtil.isNotNull(bOrganization) && ObjectUtil.equals(bOrganization.getOrgType(), OrgTypeEnum.GROUP.getCode())) {
            orgId = null;
        }

        return new StatisticalDataParam()
                .setDimension(req.getDimension())
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setTenantId(UserInfoContextUtils.getCurrentTenantId())
                .setOrgId(orgId)
                .setStatDims(req.getStatDims());
    }

    @SneakyThrows
    public void exportStatistic(HttpServletResponse response, OperationDashboardReq req) {
        InputStream templateInputStream = ResourceUtils.getURL("classpath:template/运营看板.xls").openStream();
        String fileMainName = "运营看板";
        if (req.getExportType() != null && req.getExportType().equals(ExcelUtils.EXPORT_TYPE_PDF)) {
            // 输出 Excel
            String excelPath = ExcelUtils.TMP_DIR + fileMainName + IdUtil.fastSimpleUUID() + ExcelTypeEnum.XLS.getValue();
            FileUtil.mkParentDirs(excelPath);

            ExcelWriter excelWriter = EasyExcelFactory.write(excelPath)
                    .withTemplate(templateInputStream)
                    .autoCloseStream(false)
                    .excelType(ExcelTypeEnum.XLS)
                    .build();
            finisWrite(req, excelWriter);
            ExcelUtils.writeExcelToPdf(response, excelPath, fileMainName);
        } else {
            String filename = URLEncoder.encode(fileMainName + DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_PATTERN) + ExcelTypeEnum.XLS.getValue(), StandardCharsets.UTF_8.name());
            response.addHeader("Content-Disposition", "attachment;filename=" + filename);
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            // 输出 Excel
            ExcelWriter excelWriter = EasyExcelFactory.write(response.getOutputStream())
                    .withTemplate(templateInputStream)
                    .autoCloseStream(false)
                    .excelType(ExcelTypeEnum.XLS)
                    .build();
            finisWrite(req, excelWriter);
        }

    }

    private void finisWrite(OperationDashboardReq req, ExcelWriter excelWriter) {
        WriteSheet writeSheet = EasyExcelFactory.writerSheet().build();

        // 头部统计指标
        Map<String, Object> dataMap = buildDataMap(req);
        excelWriter.fill(dataMap, writeSheet);

        // 用户增长数据
        DashboardStatResult userGrowthData = this.newUserGrowthData(req);
        DashboardStatResult.StatObj recommendUserCount = userGrowthData.getStatObjs()
                .stream().filter(o -> o.getStatName().equals("推广客户数")).findFirst().orElse(new DashboardStatResult.StatObj());
        DashboardStatResult.StatObj newUserCount = userGrowthData.getStatObjs()
                .stream().filter(o -> o.getStatName().equals("新增客户数")).findFirst().orElse(new DashboardStatResult.StatObj());
        List<DashboardStatResult.MergeStatObj> userGrowthList = buildDataList(recommendUserCount, newUserCount);

        // 工单合同数量数据
        DashboardStatResult orderContractQtyData = this.orderContractQtyData(req);
        DashboardStatResult.StatObj orderCount = orderContractQtyData.getStatObjs()
                .stream().filter(o -> o.getStatName().equals("工单")).findFirst().orElse(new DashboardStatResult.StatObj());
        DashboardStatResult.StatObj contractCount = orderContractQtyData.getStatObjs()
                .stream().filter(o -> o.getStatName().equals("合同")).findFirst().orElse(new DashboardStatResult.StatObj());
        List<DashboardStatResult.MergeStatObj> orderContractQtyList = buildDataList(orderCount, contractCount);

        // 工单合同金额数据
        DashboardStatResult orderContractAmountData = this.orderContractAmountData(req);
        DashboardStatResult.StatObj newAmount = orderContractAmountData.getStatObjs()
                .stream().filter(o -> o.getStatName().equals("新增合同金额")).findFirst().orElse(new DashboardStatResult.StatObj());
        DashboardStatResult.StatObj sumAmount = orderContractAmountData.getStatObjs()
                .stream().filter(o -> o.getStatName().equals("合同总金额")).findFirst().orElse(new DashboardStatResult.StatObj());
        List<DashboardStatResult.MergeStatObj> orderContractAmountList = buildDataList(newAmount, sumAmount);

        // 投诉数量占比数据
        DashboardStatResult complainPctData = this.complainPctData(req);
        DashboardStatResult.StatObj orderComplainCount = complainPctData.getStatObjs()
                .stream().filter(o -> o.getStatName().equals("工单") && o.getStatUnit().equals("件")).findFirst().orElse(new DashboardStatResult.StatObj());
        DashboardStatResult.StatObj contractComplainCount = complainPctData.getStatObjs()
                .stream().filter(o -> o.getStatName().equals("合同") && o.getStatUnit().equals("件")).findFirst().orElse(new DashboardStatResult.StatObj());
        DashboardStatResult.StatObj orderComplainPct = complainPctData.getStatObjs()
                .stream().filter(o -> o.getStatName().equals("工单") && o.getStatUnit().equals("%")).findFirst().orElse(new DashboardStatResult.StatObj());
        DashboardStatResult.StatObj contractComplainPct = complainPctData.getStatObjs()
                .stream().filter(o -> o.getStatName().equals("合同") && o.getStatUnit().equals("%")).findFirst().orElse(new DashboardStatResult.StatObj());
        List<DashboardStatResult.MergeStatObj> complainCountDataList = buildDataList(orderComplainCount, contractComplainCount);
        List<DashboardStatResult.MergeStatObj> complainPctDataList = buildDataList(orderComplainPct, contractComplainPct);

        // 投诉数量数据
        DashboardStatResult complainQtyData = this.complainQtyData(req);
        DashboardStatResult.StatObj orderComplainQty = complainQtyData.getStatObjs()
                .stream().filter(o -> o.getStatName().equals("工单")).findFirst().orElse(new DashboardStatResult.StatObj());
        DashboardStatResult.StatObj contractComplainQty = complainQtyData.getStatObjs()
                .stream().filter(o -> o.getStatName().equals("合同")).findFirst().orElse(new DashboardStatResult.StatObj());
        List<DashboardStatResult.MergeStatObj> complainQtyDataList = buildDataList(orderComplainQty, contractComplainQty);

        // 设置横向填充方向
        FillConfig fillConfig = FillConfig.builder().direction(WriteDirectionEnum.HORIZONTAL).build();
        excelWriter.fill(new FillWrapper("userGrowthList", userGrowthList), fillConfig, writeSheet);
        excelWriter.fill(new FillWrapper("orderContractQtyList", orderContractQtyList), fillConfig, writeSheet);
        excelWriter.fill(new FillWrapper("orderContractAmountList", orderContractAmountList), fillConfig, writeSheet);
        excelWriter.fill(new FillWrapper("complainCountDataList", complainCountDataList), fillConfig, writeSheet);
        excelWriter.fill(new FillWrapper("complainPctDataList", complainPctDataList), fillConfig, writeSheet);
        excelWriter.fill(new FillWrapper("complainQtyDataList", complainQtyDataList), fillConfig, writeSheet);
        excelWriter.finish();
    }

    private List<DashboardStatResult.MergeStatObj> buildDataList(DashboardStatResult.StatObj recommendUserCount, DashboardStatResult.StatObj newUserCount) {
        List<DashboardStatResult.MergeStatObj> newUserGrowthData = new ArrayList<>();
        for (int i = 0; i < recommendUserCount.getStatItems().size(); i++) {
            DashboardStatResult.MergeStatObj mergeStatObj = new DashboardStatResult.MergeStatObj();
            mergeStatObj.setDimVal(recommendUserCount.getStatItems().get(i).getItemName());
            mergeStatObj.setStatVal1(recommendUserCount.getStatItems().get(i).getItemVal().toString());
            mergeStatObj.setStatVal2(newUserCount.getStatItems().get(i).getItemVal().toString());
            newUserGrowthData.add(mergeStatObj);
        }
        return newUserGrowthData;
    }

    private Map<String, Object> buildDataMap(OperationDashboardReq req) {
        // easyexcel 不支持直接使用嵌套对象填充数据，需要对嵌套的对象进行转换处理
        Map<String, Object> dataMap = BeanUtil.beanToMap(req);
        dataMap.put("dimension", req.getDimension().equals("week") ? "周" : "月");
        dataMap.put("orgName", "全部");
        if (req.getOrgId() != null) {
            dataMap.put("orgName", Optional.ofNullable(bOrganizationDao.getById(req.getOrgId())).map(BOrganization::getName).orElse(StrPool.DASHED));
        }

        // 客户、工单、合同统计数据
        StatisticalDataResult statResult = this.statisticalData(req);
        dataMap.put("userRecommendCount", statResult.getUser().getRecommendCount());
        dataMap.put("userNewCount", statResult.getUser().getNewCount());
        dataMap.put("userTotalCount", statResult.getUser().getTotalCount());
        dataMap.put("consultingNewCount", statResult.getConsulting().getNewCount());
        dataMap.put("consultingTotalCount", statResult.getConsulting().getTotalCount());
        dataMap.put("contractNewCount", statResult.getContract().getNewCount());
        dataMap.put("contractTotalCount", statResult.getContract().getTotalCount());
        dataMap.put("contractNewAmount", Optional.ofNullable(statResult.getContract().getNewAmount()).map(String::valueOf).orElse(""));
        dataMap.put("contractTotalAmount", Optional.ofNullable(statResult.getContract().getTotalAmount()).map(String::valueOf).orElse(""));
        return dataMap;
    }


    public void exportPromoteUserData(HttpServletResponse response, OperationDashboardReq req) {
        Integer orgId = req.getOrgId();
        BOrganization bOrganization = bOrganizationDao.getById(req.getOrgId());
        // 选择集团则看所有业务公司总数据
        if (ObjectUtil.isNotNull(bOrganization) && ObjectUtil.equals(bOrganization.getOrgType(), OrgTypeEnum.GROUP.getCode())) {
            orgId = null;
        }
        StatisticalDataParam param = new StatisticalDataParam();
        param.setOrgId(orgId);
        List<PromoteUserDataResult> data = operationDataDomainService.getPromoteUserData(param);

        //数据转为要导出的dto类
        List<PromoteUserDataExportDto> exportDtoList = BeanUtil.copyToList(data, PromoteUserDataExportDto.class);
        String filename = "E能管家推广用户-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".xls";
        //ExcelUtils写excel 响应给前端
        ExcelUtils.write(response, filename, "E能管家推广用户数据", PromoteUserDataExportDto.class, exportDtoList);
    }

}
