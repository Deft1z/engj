package com.kge.energy.crm.resource.service;

import com.kge.energy.crm.repository.dao.BResourceDao;
import com.kge.energy.crm.repository.entityext.result.ResourcePermissionResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangjihua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BResourceService {

    private final BResourceDao bResourceDao;

    public List<ResourcePermissionResult> findPermission(Integer userId, List<String> urls) {
        return bResourceDao.findPermission(userId, urls);
    }

}
