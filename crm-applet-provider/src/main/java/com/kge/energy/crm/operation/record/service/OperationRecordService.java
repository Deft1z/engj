package com.kge.energy.crm.operation.record.service;

import cn.hutool.core.util.ObjectUtil;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.OrgTypeEnum;
import com.kge.energy.crm.operation.data.resp.OperationDataOrgResp;
import com.kge.energy.crm.operation.data.service.OperationDataDomainService;
import com.kge.energy.crm.operation.record.req.OperationRecordReq;
import com.kge.energy.crm.repository.dao.BOrganizationDao;
import com.kge.energy.crm.repository.entity.BOrganization;
import com.kge.energy.crm.repository.entityext.param.StatisticalDataParam;
import com.kge.energy.crm.repository.entityext.result.StatisticalDataResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author wangjihua
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class OperationRecordService {

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
    public StatisticalDataResult statisticalData(OperationRecordReq req) {
        return operationDataDomainService.statisticalData(defaultStatisticalDataParam(req));
    }

    private StatisticalDataParam defaultStatisticalDataParam(OperationRecordReq req) {
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
                .setOrgId(orgId);
    }


}
