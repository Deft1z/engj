package com.kge.energy.crm.common.go;

import com.kge.platform.framework.common.util.ThreadLocalUtils;
import com.kge.platform.framework.web.interceptor.DelegatedOrderedInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

/**
 * @author wangjihua
 */
@Component
public class ConvertToGoFormatsInterceptor implements DelegatedOrderedInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            boolean isConvertToGoFormats = handlerMethod.getMethod().isAnnotationPresent(ConvertToGoFormats.class);

            ThreadLocalUtils.put("IS_CONVERT_TO_GO_FORMATS", isConvertToGoFormats);
        }

        return true;
    }
}
