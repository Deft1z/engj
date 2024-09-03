package com.kge.energy.crm.common.constans;

import java.util.concurrent.TimeUnit;

/**
 * @author wangjihua
 */
public class TokenConstant {

    public static final String HEADER_KEY = "Authorization";

    public static final long PC_EXPIRED_TIMEOUT = 1L;

    public static final TimeUnit PC_EXPIRED_TIMEUNIT = TimeUnit.HOURS;

    public static final long APPLET_EXPIRED_TIMEOUT = 14 * 24L;

    public static final TimeUnit APPLET_EXPIRED_TIMEUNIT = TimeUnit.HOURS;

}
