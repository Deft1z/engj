package com.kge.energy.crm.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.kge.energy.crm.enums.RoleEnums;
import com.kge.platform.framework.common.exception.ServiceException;

/**
 * @author wangjihua
 */
public class AuthVerifyUtils {

    private AuthVerifyUtils() {
    }

    /**
     * 是否超级管理员
     */
    public static boolean isSuperAdmin() {
        return isContainsRole(RoleEnums.SUPER_ADMIN.getCode());
    }

    /**
     * 不是超级管理员
     */
    public static boolean notSuperAdmin() {
        return !isSuperAdmin();
    }

    /**
     * 是否租户管理员
     */
    public static boolean isTenantAdmin() {
        return isContainsRole(RoleEnums.TENANT_ADMIN.getCode());
    }

    /**
     * 不是租户管理员
     */
    public static boolean notTenantAdmin() {
        return !isTenantAdmin();
    }

    public static void mustSuperAdmin() {
        if (!isSuperAdmin()) {
            throw new ServiceException("当前用户非超级管理员用户");
        }
    }

    /**
     * 是否仅是小程序用户
     */
    public static boolean isOnlyAppletUser() {
        return CollUtil.size(UserInfoContextUtils.getCurrentUserInfo().getRoleCodes()) == 1
                && isContainsRole(RoleEnums.APPLET_USER.getCode());
    }

    public static void mustTenantAdmin() {
        if (!isTenantAdmin()) {
            throw new ServiceException("当前用户非租户管理员用户");
        }
    }

    public static void mustAdmin() {
        if (notSuperAdmin() && notTenantAdmin()) {
            throw new ServiceException("当前用户非管理员用户");
        }
    }

    /**
     * 当前登录用户是否包含某个角色
     */
    public static boolean isContainsRole(String roleCode) {
        return UserInfoContextUtils.getCurrentUserInfo()
                .getRoleCodes()
                .contains(roleCode);
    }

    public static boolean isGreaterOrEqualBLevel() {
        String jobLevel = UserInfoContextUtils.getCurrentUserInfo().getJobLevel();
        return StrUtil.equals(jobLevel, "A") || StrUtil.equals(jobLevel, "B");
    }
}
