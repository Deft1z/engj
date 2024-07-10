package com.kge.energy.crm.org.service;

import cn.hutool.core.bean.BeanUtil;
import com.kge.energy.crm.org.resp.OrgDictResp;
import com.kge.energy.crm.repository.dao.BOrganizationDao;
import com.kge.energy.crm.repository.entityext.result.OrgDictResult;
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
public class OrgService {

    private final BOrganizationDao bOrganizationDao;

    public List<OrgDictResp> getOrgDictList() {

        List<OrgDictResult> orgDictResults = bOrganizationDao.getOrgDictList();

        return BeanUtil.copyToList(orgDictResults, OrgDictResp.class);
    }
}
