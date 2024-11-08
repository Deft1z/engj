package com.kge.energy.crm.common.interceptor;

import com.kge.energy.crm.auth.service.AuthDomainService;
import com.kge.energy.crm.common.property.AuthProperties;
import com.kge.energy.crm.common.util.RedisUtils;
import com.kge.energy.crm.user.service.UserDomainService;
import com.kge.platform.framework.web.interceptor.DelegatedOrderedInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

/**
 * 体验数据重定向拦截器
 *
 * @author wangjihua
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExperienceRedirectInterceptor implements DelegatedOrderedInterceptor {

    private final RedisUtils redisUtils;

    private final UserDomainService userDomainService;

    private final AuthProperties authProperties;

    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    private final AuthDomainService authDomainService;

    @Override
    public int getOrder() {
        return 1001;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String originalUrl = request.getRequestURI();
        if (originalUrl.equals("/old-endpoint")) {
            response.sendRedirect("/new-endpoint");
            return false; // 阻止请求继续
        }

        return true;
    }


}
