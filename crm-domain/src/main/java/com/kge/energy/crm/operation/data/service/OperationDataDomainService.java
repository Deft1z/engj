package com.kge.energy.crm.operation.data.service;

import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.OrgTypeEnum;
import com.kge.energy.crm.enums.RoleEnums;
import com.kge.energy.crm.operation.data.resp.OperationDataOrgResp;
import com.kge.energy.crm.repository.dao.BOrganizationDao;
import com.kge.energy.crm.repository.dao.OperationDataDao;
import com.kge.energy.crm.repository.entityext.param.StatisticalDataParam;
import com.kge.energy.crm.repository.entityext.result.DashboardStatResult;
import com.kge.energy.crm.repository.entityext.result.StatisticalDataResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public StatisticalDataResult statisticalData(StatisticalDataParam param) {

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

    /**
     * 新用户增长曲线数据
     */
    public DashboardStatResult newUserGrowthData(StatisticalDataParam param) {
        return operationDataDao.getNewUserGrowthStat(param);
    }

    /**
     * 工单合同数量变化
     * @param param
     * @return
     */
    public DashboardStatResult orderContractQtyData(StatisticalDataParam param) {
        return operationDataDao.getOrderContractQtyStat(param);
    }

    /**
     * 合同金额变化
     * @param param
     * @return
     */
    public DashboardStatResult orderContractAmountData(StatisticalDataParam param) {
        return operationDataDao.getOrderContractAmountStat(param);
    }

    /**
     * 投诉类型占比
     * @param param
     * @return
     */
    public DashboardStatResult complainPctData(StatisticalDataParam param) {
        return operationDataDao.getComplainPctStat(param);
    }

    /**
     * 投诉类型数量变化
     * @param param
     * @return
     */
    public DashboardStatResult complainQtyData(StatisticalDataParam param) {
        return operationDataDao.getComplainQtyStat(param);
    }

}
