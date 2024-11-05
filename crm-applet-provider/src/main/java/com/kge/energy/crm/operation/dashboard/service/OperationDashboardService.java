package com.kge.energy.crm.operation.dashboard.service;

import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.operation.dashboard.req.OperationDashboardReq;
import com.kge.energy.crm.operation.data.resp.OperationDataOrgResp;
import com.kge.energy.crm.operation.data.service.OperationDataDomainService;
import com.kge.energy.crm.repository.entityext.result.StatisticalDataResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wangjihua
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class OperationDashboardService {

    private final OperationDataDomainService operationDataDomainService;

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
        return operationDataDomainService.statisticalData(
                req.getStartTime(), req.getEndTime(), UserInfoContextUtils.getCurrentTenantId(), req.getOrgId()
        );
    }


}
