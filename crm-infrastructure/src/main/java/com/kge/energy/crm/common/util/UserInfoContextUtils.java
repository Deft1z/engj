package com.kge.energy.crm.common.util;

import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.platform.framework.common.util.CommonUserInfoContextUtils;

/**
 * 用户上下文工具类
 *
 * @author wangjihua
 */
public class UserInfoContextUtils {

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


}
