package com.kge.energy.crm.common.interceptor;

import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.execption.BadException;
import com.kge.energy.crm.common.net.ResponseCode;
import com.kge.energy.crm.common.property.AuthProperties;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.user.service.UserDomainService;
import com.kge.platform.framework.web.interceptor.DelegatedOrderedInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author wangjihua
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenInterceptor implements DelegatedOrderedInterceptor {

    private final StringRedisTemplate stringRedisTemplate;

    private final UserDomainService userDomainService;

    private final AuthProperties authProperties;

    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    @Override
    public int getOrder() {
        return 1000;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String method = request.getMethod();
        String url = request.getRequestURI();
        log.info("{} {}", method, url);

        if (StrUtil.equalsIgnoreCase(method, "OPTIONS")) {
            return true;
        }

        boolean isTokenWhiteUrl = authProperties.getToken()
                .getWhiteList()
                .stream()
                .anyMatch(item -> ANT_PATH_MATCHER.match(item, url));
        if (isTokenWhiteUrl) {
            return true;
        }

        String authToken = request.getHeader("Authorization");
        if (StrUtil.isBlank(authToken)) {
            throw new BadException(ResponseCode.TOKEN_FAIL);
        }

        String uid = stringRedisTemplate.opsForValue().get(authProperties.getToken().getRedisFront() + authToken);
        if (StrUtil.isBlank(uid)) {
            throw new BadException(ResponseCode.TOKEN_FAIL);
        }
        Integer userId = Integer.valueOf(uid);

        // 设置用户上下文信息
        putUserInfo("mgr", userId);

        return true;
    }


    /**
     * 设置用户上下文信息
     */
    private void putUserInfo(String systemType, Integer userId) {

        UserInfoDto userInfoDto = userDomainService.findUserInfoDto(systemType, userId);
        if (ObjUtil.isNull(userInfoDto)) {
            throw new BadException(ResponseCode.TOKEN_FAIL);
        }

        UserInfoContextUtils.putUserInfo(userInfoDto);
    }


}
