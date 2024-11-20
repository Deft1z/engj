package com.kge.energy.crm.operation.dashboard.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.OrgTypeEnum;
import com.kge.energy.crm.operation.dashboard.req.OperationDashboardReq;
import com.kge.energy.crm.operation.data.resp.OperationDataOrgResp;
import com.kge.energy.crm.operation.data.service.OperationDataDomainService;
import com.kge.energy.crm.repository.dao.BOrganizationDao;
import com.kge.energy.crm.repository.entity.BOrganization;
import com.kge.energy.crm.repository.entityext.param.StatisticalDataParam;
import com.kge.energy.crm.repository.entityext.result.DashboardStatResult;
import com.kge.energy.crm.repository.entityext.result.StatisticalDataResult;
import com.kge.platform.framework.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
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
            statDims.add(year + StrUtil.padPre(String.valueOf(week), 2, "0"));
            startTime = startTime.plusDays(7);
            if (startTime.isBefore(endTime) || startTime.isEqual(endTime)) {
                genStatDims(statDims, startTime, endTime, dimension);
            }
        } else {
            int month = startTime.getMonthValue();
            statDims.add(year + StrUtil.padPre(String.valueOf(month), 2, "0"));
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

}
