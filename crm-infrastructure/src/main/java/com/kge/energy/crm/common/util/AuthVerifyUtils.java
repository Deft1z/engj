package com.kge.energy.crm.common.util;

import cn.hutool.core.util.StrUtil;
import com.kge.platform.framework.common.exception.ServiceException;

/**
 * @author wangjihua
 */
public class AuthVerifyUtils {

    private static final String SUPER_ADMIN = "super_admin";

    private static final String TENANT_ADMIN = "tenant_admin";

    private AuthVerifyUtils() {
    }

    /**
     * 是否超级管理员
     */
    public static boolean isSuperAdmin() {
        return UserInfoContextUtils.getCurrentUserInfo().getRoleList()
                .stream()
                .anyMatch(role -> StrUtil.equals(role.getCode(), SUPER_ADMIN));
    }

    /**
     * 是否租户管理员
     */
    public static boolean isTenantAdmin() {
        return UserInfoContextUtils.getCurrentUserInfo().getRoleList()
                .stream()
                .anyMatch(role -> StrUtil.equals(role.getCode(), TENANT_ADMIN));
    }


    public static void mustSuperAdmin() {
        if (!isSuperAdmin()) {
            throw new ServiceException("当前用户非超级管理员用户");
        }
    }

    public static void mustTenantAdmin() {
        if (!isTenantAdmin()) {
            throw new ServiceException("当前用户非租户管理员用户");
        }
    }

    public static void mustAdmin() {
        if (!isSuperAdmin() && !isTenantAdmin()) {
            throw new ServiceException("当前用户非管理员用户");
        }
    }
}
