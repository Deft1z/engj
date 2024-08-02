package com.kge.energy.crm.resource.service;

import com.kge.energy.crm.CrmAppletProvider;
import com.kge.energy.crm.resource.req.SystemResourceReq;
import com.kge.energy.crm.resource.req.UserResourceReq;
import com.kge.energy.crm.resource.resp.ResourceListResp;
import com.kge.platform.framework.web.util.JsonUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author wangjihua
 */
@Slf4j
//@RequiredArgsConstructor
@SpringBootTest(classes = CrmAppletProvider.class)
class ResourceDomainServiceTest {

    @Resource
    private ResourceDomainService resourceDomainService;

    @Test
    void getSystemResources() {
        ResourceListResp systemResources = resourceDomainService.getSystemResources(new SystemResourceReq());
        log.info("systemResources: {}", JsonUtils.serialize(systemResources));
    }

    @Test
    void getUserResources() {

        UserResourceReq req = new UserResourceReq().setUserId(2568)
                .setSystemType("applet")
                .setTenantId(1);
        ResourceListResp userResources = resourceDomainService.getUserResources(req);
        log.info("userResources: {}", JsonUtils.serialize(userResources));
    }

}
