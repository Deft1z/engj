package com.kge.energy.crm.common.interceptor;

import cn.hutool.core.util.ObjectUtil;
import com.kge.energy.crm.common.property.ExperienceDataProperties;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.platform.framework.web.interceptor.DelegatedOrderedInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private final ExperienceDataProperties experienceDataProperties;

    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    @Override
    public int getOrder() {
        return 1001;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (UserInfoContextUtils.getCurrentUserInfo() != null && ObjectUtil.notEqual(UserInfoContextUtils.getCurrentUserInfo().getTenantName(), experienceDataProperties.getTenantName())) {
            return true;
        }
        
        boolean isRedirectUrl = experienceDataProperties.getRedirectUrlList().stream()
                .anyMatch(item -> ANT_PATH_MATCHER.match(item, request.getRequestURI()));
        if (!isRedirectUrl) {
            return true;
        }

        String url = "/experience" + request.getRequestURI().replace(contextPath, "");

        request.getRequestDispatcher(url)
                .forward(request, response);
        return false;
    }


}
