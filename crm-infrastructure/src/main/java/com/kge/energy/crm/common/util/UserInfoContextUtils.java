package com.kge.energy.crm.common.util;

import cn.hutool.core.util.StrUtil;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.platform.framework.common.util.CommonUserInfoContextUtils;

/**
 * 用户上下文工具类
 *
 * @author wangjihua
 */
public class UserInfoContextUtils {

    private static final String SUPER_ADMIN = "super_admin";

    private static final String TENANT_ADMIN = "tenant_admin";

    private UserInfoContextUtils() {
    }

    public static UserInfoDto getCurrentUserInfo() {
        return CommonUserInfoContextUtils.getUserInfoExt(UserInfoDto.class);
    }

    public static Integer getCurrentUserId() {
        return Math.toIntExact(getCurrentUserInfo().getUserId());
    }

    public static String getCurrentUserName() {
        return getCurrentUserInfo().getUserName();
    }

    public static String getCurrentRealName() {
        return getCurrentUserInfo().getRealname();
    }

    public static String getCurrentMobile() {
        return getCurrentUserInfo().getMobile();
    }

    public static Integer getCurrentTenantId() {
        return getCurrentUserInfo().getTenantId();
    }

    public static String getCurrentSystemType() {
        return getCurrentUserInfo().getSystemType();
    }


    public static void putUserInfo(UserInfoDto userInfoDto) {
        CommonUserInfoContextUtils.putUserInfo(userInfoDto);
    }

    /**
     * 是否超级管理员
     */
    public static boolean isSuperAdmin() {
        return getCurrentUserInfo().getRoleList()
                .stream()
                .anyMatch(role -> StrUtil.equals(role.getCode(), SUPER_ADMIN));
    }

    /**
     * 是否租户管理员
     */
    public static boolean isTenantAdmin() {
        return getCurrentUserInfo().getRoleList()
                .stream()
                .anyMatch(role -> StrUtil.equals(role.getCode(), TENANT_ADMIN));
    }

}
