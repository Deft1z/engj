package com.kge.energy.crm.permission.service;

import cn.hutool.core.collection.CollectionUtil;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.BizFunctionEnums;
import com.kge.energy.crm.enums.DataPermissionRangeTypeEnums;
import com.kge.energy.crm.enums.RoleEnums;
import com.kge.energy.crm.repository.dao.BRoleDao;
import com.kge.energy.crm.repository.dao.CfDataPermissionDao;
import com.kge.energy.crm.repository.entity.BRole;
import com.kge.energy.crm.repository.entity.CfDataPermission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DataPermissionDomainService {

    private final CfDataPermissionDao cfDataPermissionDao;

    private final BRoleDao bRoleDao;

    public DataPermissionRangeTypeEnums getCurrentUserDataPermission(BizFunctionEnums bizFunctionEnums) {

        return getCurrentUserDataPermission(bizFunctionEnums.getCode());
    }

    public DataPermissionRangeTypeEnums getCurrentUserDataPermission(String functionCode) {

        UserInfoDto userInfoDto = UserInfoContextUtils.getCurrentUserInfo();

        return getDataPermission(userInfoDto.getTenantId(), userInfoDto.getRoleIds(), functionCode);
    }

    public DataPermissionRangeTypeEnums getDataPermission(Integer tenantId, Set<Integer> roleIds, String functionCode) {

        List<CfDataPermission> cfDataPermissions = cfDataPermissionDao.getDataPermission(tenantId, roleIds, functionCode);

        if (CollectionUtil.isEmpty(cfDataPermissions)) {
            return DataPermissionRangeTypeEnums.PERSONAL;
        }

        return DataPermissionRangeTypeEnums.getByCode(cfDataPermissions.get(0).getDataRangeType());
    }

    /**
     * 获取业务功能的角色范围
     */
    public List<RoleEnums> getFunctionRoleEnums(Integer tenantId, String functionCode) {

        List<CfDataPermission> cfDataPermissions = cfDataPermissionDao.getDataPermission(tenantId, null, functionCode);

        if (CollectionUtil.isEmpty(cfDataPermissions)) {
            return Collections.emptyList();
        }

        List<BRole> bRoles = bRoleDao.listByIds(cfDataPermissions.stream().map(CfDataPermission::getRoleId).toList());

        return bRoles.stream()
                .map(brole -> RoleEnums.getByCode(brole.getCode()))
                .toList();
    }
}
