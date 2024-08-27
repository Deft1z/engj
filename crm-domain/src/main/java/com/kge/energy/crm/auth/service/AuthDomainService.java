package com.kge.energy.crm.auth.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.text.AntPathMatcher;
import com.kge.energy.crm.common.util.RedisUtils;
import com.kge.energy.crm.repository.dao.BResourceInterfaceDao;
import com.kge.energy.crm.repository.dao.BRoleDao;
import com.kge.energy.crm.repository.entity.BResourceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthDomainService {

    private final BResourceInterfaceDao resourceInterfaceDao;

    private final BRoleDao bRoleDao;

    private final RedisUtils redisUtils;

    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    private static final String AUTH_SYSTEM_INTERFACE_CACHE_KEY = "auth:%s:interface:set";

    private static final String AUTH_SYSTEM_ROLE_INTERFACE_CACHE_KEY = "auth:%s:role:interface:%s";

    /**
     * 获取请求地址存储模板
     */
    private String getRequestUrlPattern(String requestMethod, String url) {
        return requestMethod.toUpperCase() + "_" + url;
    }

    /*
     * auth:业务系统类型:role:interface:角色ID，eg: auth:mgr:role:interface:1
     */
    private String getSystemInterfaceCacheKey(String systemType) {
        return String.format(AUTH_SYSTEM_INTERFACE_CACHE_KEY, systemType);
    }

    /*
     * auth:业务系统类型:role:interface:角色ID，eg: auth:mgr:role:interface:1
     */
    private String getSystemRoleInterfaceCacheKey(String systemType, Integer roleId) {
        return String.format(AUTH_SYSTEM_ROLE_INTERFACE_CACHE_KEY, systemType, roleId);
    }

    /**
     * 清除系统权限接口缓存
     */
    public void deleteSystemInterfaceCache(String systemType) {
        String key = getSystemInterfaceCacheKey(systemType);
        redisUtils.delete(key);

        bRoleDao.list()
                .forEach(role -> deleteSystemRoleInterfaceCache(systemType, role.getRoleId()));
    }

    /**
     * 刷新系统权限接口缓存
     */
    public Set<String> refreshSystemInterfaceCache(String systemType) {

        Set<String> authInterfaces = Collections.emptySet();

        String key = getSystemInterfaceCacheKey(systemType);

        List<BResourceInterface> bResourceInterfaces = resourceInterfaceDao.listBySystemType(systemType);
        if (CollectionUtil.isEmpty(bResourceInterfaces)) {
            redisUtils.delete(key);
            return authInterfaces;
        }

        authInterfaces = bResourceInterfaces
                .stream()
                .map(bi -> getRequestUrlPattern(bi.getRequestMethod(), bi.getInterfaceUrl()))
                .collect(Collectors.toSet());

        redisUtils.sAdd(key, authInterfaces.toArray(String[]::new));
        redisUtils.expire(key, 24, TimeUnit.HOURS);

        return authInterfaces;
    }


    /**
     * 清除系统角色权限接口缓存
     */
    public void deleteSystemRoleInterfaceCache(String systemType, Integer roleId) {
        String key = getSystemRoleInterfaceCacheKey(systemType, roleId);
        redisUtils.delete(key);
    }

    /**
     * 刷新系统角色权限接口缓存
     */
    public Set<String> refreshSystemRoleInterfaceCache(String systemType, Integer roleId) {

        Set<String> authInterfaces = Collections.emptySet();

        String key = getSystemRoleInterfaceCacheKey(systemType, roleId);

        List<BResourceInterface> bResourceInterfaces = resourceInterfaceDao.listByRole(systemType, roleId);
        if (CollectionUtil.isEmpty(bResourceInterfaces)) {
            redisUtils.delete(key);
            return authInterfaces;
        }

        authInterfaces = bResourceInterfaces
                .stream()
                .map(bi -> getRequestUrlPattern(bi.getRequestMethod(), bi.getInterfaceUrl()))
                .collect(Collectors.toSet());

        redisUtils.sAdd(key, authInterfaces.toArray(String[]::new));
        redisUtils.expire(key, 24, TimeUnit.HOURS);

        return authInterfaces;
    }

    /**
     * 判断是否权限接口
     */
    public boolean isAuthInterface(String systemType, String requestMethod, String url) {

        String matchPath = getRequestUrlPattern(requestMethod, url);

        Set<String> authInterfaces = redisUtils.setMembers(getSystemInterfaceCacheKey(systemType));

        if (CollectionUtil.isEmpty(authInterfaces)) {
            authInterfaces = refreshSystemInterfaceCache(systemType);
        }

        return authInterfaces.stream()
                .anyMatch(authInterface -> ANT_PATH_MATCHER.match(authInterface, matchPath));
    }

    /**
     * 检查用户角色是否有访问接口的权限
     */
    public boolean roleHasInterfacePermission(Set<Integer> roleIds, String systemType, String requestMethod, String url) {

        if (CollectionUtil.isEmpty(roleIds)) {
            return true;
        }

        String matchPath = getRequestUrlPattern(requestMethod, url);

        return roleIds.stream()
                .anyMatch(roleId -> {
                    Set<String> authInterfaces = redisUtils.setMembers(getSystemRoleInterfaceCacheKey(systemType, roleId));

                    if (CollectionUtil.isEmpty(authInterfaces)) {
                        authInterfaces = refreshSystemRoleInterfaceCache(systemType, roleId);
                    }

                    return authInterfaces.stream()
                            .anyMatch(authInterface -> ANT_PATH_MATCHER.match(authInterface, matchPath));
                });
    }


}
