package com.kge.energy.crm.common.interceptor;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.execption.BadException;
import com.kge.energy.crm.common.net.ResponseCode;
import com.kge.energy.crm.common.property.AuthProperties;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.repository.entity.BUser;
import com.kge.energy.crm.user.service.UserDomainService;
import com.kge.platform.framework.web.interceptor.DelegatedOrderedInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

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

    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public int getOrder() {
        return 1000;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String url = request.getRequestURI();
        log.info("url:{}", url);

        boolean isTokenWhiteUrl = authProperties.getToken()
                .getWhiteList()
                .stream()
                .anyMatch(item -> antPathMatcher.match(item, url));
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

        // 设置用户上下文信息
        UserInfoDto userInfoDto = putUserInfo(Integer.valueOf(uid));
        if (CollectionUtil.isEmpty(userInfoDto.getOrganizationList())) {
            log.error("找不到用户对应的租户ID: {}", userInfoDto.getUserId());
        }

        return true;
    }


    /**
     * 设置用户上下文信息
     */
    private UserInfoDto putUserInfo(Integer uid) {

        BUser user = userDomainService.getBUserById(uid);

        if (ObjUtil.isNull(user)) {
            throw new BadException(ResponseCode.TOKEN_FAIL);
        }

        UserInfoDto userInfoDto = userDomainService.findUserInfoDto(user);
        if (ObjUtil.isNull(userInfoDto)) {
            throw new BadException(ResponseCode.TOKEN_FAIL);
        }

        UserInfoContextUtils.putUserInfo(userInfoDto);

        return userInfoDto;
    }


}
