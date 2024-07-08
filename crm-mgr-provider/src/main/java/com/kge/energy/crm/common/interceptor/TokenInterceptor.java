package com.kge.energy.crm.common.interceptor;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.kge.energy.crm.common.dto.UserInfoDto;
import com.kge.energy.crm.common.execption.BadException;
import com.kge.energy.crm.common.net.ResponseCode;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.repository.entity.BUser;
import com.kge.energy.crm.repository.entityext.result.ResourcePermissionResult;
import com.kge.energy.crm.resource.service.BResourceService;
import com.kge.energy.crm.user.service.BUserService;
import com.kge.platform.framework.common.util.ThreadLocalUtils;
import com.kge.platform.framework.web.interceptor.DelegatedOrderedInterceptor;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangjihua
 */
@Component
@RequiredArgsConstructor
public class TokenInterceptor implements DelegatedOrderedInterceptor {

    private final StringRedisTemplate stringRedisTemplate;

    private final BResourceService bResourceService;

    private final BUserService bUserService;

    @Value("${redis.tokenfront}")
    private String tokenFront;

    @Override
    public int getOrder() {
        return 1000;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String authToken = request.getHeader("Authorization");
        if (StrUtil.isBlank(authToken)) {
            throw new BadException(ResponseCode.TOKEN_FAIL);
        }

        String uid = stringRedisTemplate.opsForValue().get(tokenFront + authToken);
        Integer userId = Integer.valueOf(uid);

        handlePermission(request, userId);

        // 设置用户上下文信息
        putUserInfo(Integer.valueOf(uid));

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
        ;
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
            case "load" -> permissionResult.getAuthRead();
            case "insert", "update", "upsert", "import", "confirm", "enable", "add" -> permissionResult.getAuthWrite();
            case "delete", "batchDelete", "hardDelete", "batchHardDelete", "del" -> permissionResult.getAuthDelete();
            case "audit" -> permissionResult.getAuthAudit();
            default -> permissionResult.getAuthRead();
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

        BUser user = bUserService.getBUserById(uid);

        if (ObjUtil.isNull(user)) {
            throw new BadException(ResponseCode.TOKEN_FAIL);
        }

        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setUserId(Long.valueOf(user.getUserId()))
                .setUserName(user.getName())
                .setRealname(user.getRealname()) ;
        userInfoDto.setMobile(userInfoDto.getMobile())
                .setOpenId(userInfoDto.getOpenId())
                .setType(userInfoDto.getType());

        UserInfoContextUtils.putUserInfo(userInfoDto);
    }


}
