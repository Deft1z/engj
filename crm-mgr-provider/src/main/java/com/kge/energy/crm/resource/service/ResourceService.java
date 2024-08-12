package com.kge.energy.crm.resource.service;

import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.resource.req.SystemResourceReq;
import com.kge.energy.crm.resource.req.UserResourceReq;
import com.kge.energy.crm.resource.resp.ResourceListResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceDomainService resourceDomainService;


    public ResourceListResp currentUserResource() {

        UserResourceReq req = new UserResourceReq()
                .setUserId(UserInfoContextUtils.getCurrentUserId())
                .setSystemType(UserInfoContextUtils.getCurrentSystemType())
                .setTenantId(UserInfoContextUtils.getCurrentTenantId());

        return resourceDomainService.getUserResources(req);
    }

    /**
     * 获取系统所有菜单资源
     */
    public ResourceListResp systemResources(SystemResourceReq req) {
        return resourceDomainService.getSystemResources(req);
    }

}
