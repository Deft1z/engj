package com.kge.energy.crm.common.interceptor;

import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.execption.BadException;
import com.kge.energy.crm.common.net.ResponseCode;
import com.kge.energy.crm.common.property.AuthProperties;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.repository.entity.BUser;
import com.kge.energy.crm.repository.entityext.result.ResourcePermissionResult;
import com.kge.energy.crm.resource.service.BResourceService;
import com.kge.energy.crm.user.service.UserDomainService;
import com.kge.platform.framework.web.interceptor.DelegatedOrderedInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author wangjihua
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenInterceptor implements DelegatedOrderedInterceptor {

    private final StringRedisTemplate stringRedisTemplate;

    private final BResourceService bResourceService;

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
        Integer userId = Integer.valueOf(uid);

        // 设置用户上下文信息
        putUserInfo(Integer.valueOf(uid));

        boolean isPermissionWhiteUrl = authProperties.getPermission()
                .getWhiteList()
                .stream()
                .anyMatch(item -> antPathMatcher.match(item, url));
        if (isPermissionWhiteUrl) {
            return true;
        }

        handlePermission(request, userId);

        return true;
    }


    private void handlePermission(HttpServletRequest request, Integer userId) {

        // 查询url, 去除参数部分
        String url = request.getRequestURI();
        String[] urlArr = url.split("/");
        String[] resource = Arrays.copyOfRange(urlArr, 1, urlArr.length - 1);
        String optSign = urlArr[urlArr.length - 1];
        // 去除参数部分
        optSign = optSign.split("\\?")[0];

        ResourcePermissionResult permissionResult = new ResourcePermissionResult();

        List<ResourcePermissionResult> permissionList = bResourceService.findPermission(userId, List.of(resource));
        Map<String, ResourcePermissionResult> urlMap = new HashMap<>();
        for (ResourcePermissionResult item : permissionList) {
            urlMap.put(concatUrl(item.getName(), item, permissionList), item);
        }

        String urlTmp = String.join("/", resource);
        for (Map.Entry<String, ResourcePermissionResult> entry : urlMap.entrySet()) {
            if (ObjUtil.equals(entry.getKey(), urlTmp)) {
                permissionResult = entry.getValue();
            }
        }

        if (ObjUtil.equals(permissionResult.getResourceId(), 0)) {
            throw new BadException(ResponseCode.AUTHORITY_FAIL);
        }

        // 匹配权限
        boolean pass = switch (optSign) {
            case "load" -> Optional.ofNullable(permissionResult.getAuthRead()).orElse(Boolean.TRUE);
            case "insert", "update", "upsert", "import", "confirm", "enable", "add" ->
                    Optional.ofNullable(permissionResult.getAuthWrite()).orElse(Boolean.TRUE);
            case "delete", "batchDelete", "hardDelete", "batchHardDelete", "del" ->
                    Optional.ofNullable(permissionResult.getAuthDelete()).orElse(Boolean.TRUE);
            case "audit" -> Optional.ofNullable(permissionResult.getAuthAudit()).orElse(Boolean.TRUE);
            default -> Optional.ofNullable(permissionResult.getAuthRead()).orElse(Boolean.FALSE);
        };

        if (!pass) {
            throw new BadException(ResponseCode.AUTHORITY_FAIL);
        }
    }

    private String concatUrl(String url, ResourcePermissionResult node, List<ResourcePermissionResult> list) {

        for (ResourcePermissionResult item : list) {
            if (ObjUtil.equals(item.getResourceId(), node.getParentResourceId())) {
                return concatUrl(item.getName() + "/" + url, item, list);
            }
        }

        return url;
    }

    /**
     * 设置用户上下文信息
     */
    private void putUserInfo(Integer uid) {

        BUser user = userDomainService.getBUserById(uid);

        if (ObjUtil.isNull(user)) {
            throw new BadException(ResponseCode.TOKEN_FAIL);
        }

        UserInfoDto userInfoDto = userDomainService.findUserInfoDto(user);
        if (ObjUtil.isNull(userInfoDto)) {
            throw new BadException(ResponseCode.TOKEN_FAIL);
        }

        UserInfoContextUtils.putUserInfo(userInfoDto);
    }


}
