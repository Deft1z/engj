package com.kge.energy.crm.common.util;

import com.kge.platform.framework.common.exception.ServiceException;

/**
 * @author wangjihua
 */
public class AuthVerifyUtils {

    private AuthVerifyUtils() {
    }

    public static void mustSuperAdmin() {
        if (!UserInfoContextUtils.isSuperAdmin()) {
            throw new ServiceException("当前用户非超级管理员用户");
        }
    }

    public static void mustTenantAdmin() {
        if (!UserInfoContextUtils.isTenantAdmin()) {
            throw new ServiceException("当前用户非租户管理员用户");
        }
    }

    public static void mustAdmin() {
        if (!UserInfoContextUtils.isSuperAdmin() || !UserInfoContextUtils.isTenantAdmin()) {
            throw new ServiceException("当前用户非管理员用户");
        }
    }
}
