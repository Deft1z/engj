package com.kge.energy.crm.common.interceptor;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.kge.energy.crm.auth.service.AuthDomainService;
import com.kge.energy.crm.common.constans.TokenConstant;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.net.ResponseCode;
import com.kge.energy.crm.common.property.AuthProperties;
import com.kge.energy.crm.common.util.RedisUtils;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.enums.SystemTypeEnum;
import com.kge.energy.crm.user.service.UserDomainService;
import com.kge.platform.framework.common.exception.ServiceException;
import com.kge.platform.framework.web.interceptor.DelegatedOrderedInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wangjihua
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenInterceptor implements DelegatedOrderedInterceptor {

    private final RedisUtils redisUtils;

    private final UserDomainService userDomainService;

    private final AuthProperties authProperties;

    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    private final AuthDomainService authDomainService;

    @Override
    public int getOrder() {
        return 1000;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String method = request.getMethod();
        String url = request.getRequestURI();
        String systemType = SystemTypeEnum.APPLET.getCode();
        log.info("{} {}", request.getMethod(), url);

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

        String authToken = request.getHeader(TokenConstant.HEADER_KEY);
        if (StrUtil.isBlank(authToken)) {
            throw new ServiceException(ResponseCode.TOKEN_FAIL.getCode(), ResponseCode.TOKEN_FAIL.getMsg());
        }

        String tokenKey = authProperties.getToken().getRedisFront() + authToken;
        String uid = redisUtils.get(tokenKey);
        if (StrUtil.isBlank(uid)) {
            throw new ServiceException(ResponseCode.TOKEN_FAIL.getCode(), ResponseCode.TOKEN_FAIL.getMsg());
        }

        // 设置用户上下文信息
        UserInfoDto userInfoDto = putUserInfo(systemType, Integer.valueOf(uid));
        if (CollectionUtil.isEmpty(userInfoDto.getOrganizationList())) {
            log.error("用户无挂靠组织，用户ID: {}", userInfoDto.getUserId());
        }

        boolean isPermissionWhiteUrl = authProperties.getPermission()
                .getWhiteList()
                .stream()
                .anyMatch(item -> ANT_PATH_MATCHER.match(item, url));
        if (!isPermissionWhiteUrl && !checkPermission(systemType, method, url)) {
            log.error("用户Id {} 无权限访问接口：{}", uid, url);
            throw new ServiceException("权限不足");
        }

        // 续期
        redisUtils.expire(tokenKey, TokenConstant.APPLET_EXPIRED_TIMEOUT, TokenConstant.APPLET_EXPIRED_TIMEUNIT);

        return true;
    }


    /**
     * 检查是否有权限请求接口
     */
    private boolean checkPermission(String systemType, String method, String url) {
        if (!authDomainService.isAuthInterface(systemType, method, url)) {
            return true;
        }

        Set<Integer> roleIds = UserInfoContextUtils.getCurrentUserInfo().getRoleList().stream()
                .map(UserInfoDto.Role::getId)
                .collect(Collectors.toSet());

        return authDomainService.roleHasInterfacePermission(roleIds, systemType, method, url);
    }


    /**
     * 设置用户上下文信息
     */
    private UserInfoDto putUserInfo(String systemType, Integer userId) {

        UserInfoDto userInfoDto = userDomainService.findUserInfoDto(systemType, userId);
        if (ObjUtil.isNull(userInfoDto)) {
            throw new ServiceException(ResponseCode.TOKEN_FAIL.getCode(), ResponseCode.TOKEN_FAIL.getMsg());
        }

        UserInfoContextUtils.putUserInfo(userInfoDto);

        return userInfoDto;
    }


}
