package com.kge.energy.crm.common.constans;

import java.util.concurrent.TimeUnit;

/**
 * @author wangjihua
 */
public class TokenConstant {

    public static final String HEADER_KEY = "Authorization";

    /**
     * 存储最后一次登录的token，格式：token:last:systemType:userId
     */
    public static final String LAST_TOKEN_CACHE_KEY = "auth:token:last:%s:%s";

    public static final long PC_EXPIRED_TIMEOUT = 1L;

    public static final TimeUnit PC_EXPIRED_TIMEUNIT = TimeUnit.HOURS;

    public static final long APPLET_EXPIRED_TIMEOUT = 14 * 24L;

    public static final TimeUnit APPLET_EXPIRED_TIMEUNIT = TimeUnit.HOURS;

    /*
    * 连续登录失败次数限制
    * */
    public static final int MAX_LOGIN_ERROR_TIMES = 5;

    public static final long LOGIN_ERROR_BAN_TIME = 1;

    public static final TimeUnit LOGIN_ERROR_BAN_TIMEUNIT = TimeUnit.HOURS;

    public static final String LOGIN_ERROR_CACHE_KEY = "login:error:%s";
}
