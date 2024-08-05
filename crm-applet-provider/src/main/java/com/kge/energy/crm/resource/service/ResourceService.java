package com.kge.energy.crm.resource.service;

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

        return null;
    }
}
