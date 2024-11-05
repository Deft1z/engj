package com.kge.energy.crm.operation.data.service;

import cn.hutool.core.util.ObjectUtil;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.OrgTypeEnum;
import com.kge.energy.crm.enums.RoleEnums;
import com.kge.energy.crm.operation.data.resp.OperationDataOrgResp;
import com.kge.energy.crm.repository.dao.BOrganizationDao;
import com.kge.energy.crm.repository.dao.OperationDataDao;
import com.kge.energy.crm.repository.entity.BOrganization;
import com.kge.energy.crm.repository.entityext.param.StatisticalDataParam;
import com.kge.energy.crm.repository.entityext.result.StatisticalDataResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OperationDataDomainService {

    private final BOrganizationDao bOrganizationDao;

    private final OperationDataDao operationDataDao;

    private static final Set<RoleEnums> ALL_CURRENT_TENANT_ORG_RANGE_ROLES = Set.of(
            RoleEnums.SUPER_ADMIN, RoleEnums.TENANT_ADMIN,
            RoleEnums.JT_LEADER, RoleEnums.OPERATE_ADMIN
    );

    /**
     * 可筛选组织列表
     */
    public List<OperationDataOrgResp> orgList() {

        Set<String> roleCodes = UserInfoContextUtils.getCurrentUserInfo().getRoleCodes();
        boolean match = ALL_CURRENT_TENANT_ORG_RANGE_ROLES.stream()
                .anyMatch(roleEnum -> roleCodes.contains(roleEnum.getCode()));

        if (match) {
            Set<Integer> orgTypes = Set.of(OrgTypeEnum.GROUP.getCode(), OrgTypeEnum.COMPANY.getCode());
            return bOrganizationDao.findByTenantOrgTypes(UserInfoContextUtils.getCurrentTenantId(), orgTypes)
                    .stream()
                    .map(bOrganization -> new OperationDataOrgResp()
                            .setOrgId(bOrganization.getOrganizationId())
                            .setOrgName(bOrganization.getName()))
                    .toList();

        } else {
            return UserInfoContextUtils.getCurrentUserInfo()
                    .getOrganizationList()
                    .stream()
                    .map(bOrganization -> new OperationDataOrgResp()
                            .setOrgId(bOrganization.getId())
                            .setOrgName(bOrganization.getName()))
                    .toList();
        }
    }


    /**
     * 客户、工单、合同、投诉统计
     */
    public StatisticalDataResult statisticalData(LocalDate startTime, LocalDate endTime, Integer tenantId, Integer orgId) {

        startTime = Optional.ofNullable(startTime).orElse(LocalDate.now().withDayOfMonth(1));
        endTime = Optional.ofNullable(endTime).orElse(LocalDate.now());

        BOrganization bOrganization = bOrganizationDao.getById(orgId);
        // 选择集团则看所有业务公司总数据
        if (ObjectUtil.isNotNull(bOrganization) && ObjectUtil.equals(bOrganization.getOrgType(), OrgTypeEnum.GROUP.getCode())) {
            orgId = null;
        }

        StatisticalDataParam param = new StatisticalDataParam()
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setTenantId(tenantId)
                .setOrgId(orgId);

        StatisticalDataResult.User user = operationDataDao.getUserStatistic(param);
        StatisticalDataResult.Consulting consulting = operationDataDao.getConsultingStatistic(param);
        StatisticalDataResult.Contract contract = operationDataDao.getContractStatistic(param);
        StatisticalDataResult.Complain complain = operationDataDao.getComplainStatistic(param);

        return new StatisticalDataResult()
                .setUser(user)
                .setConsulting(consulting)
                .setContract(contract)
                .setComplain(complain);
    }

}
