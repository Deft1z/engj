package com.kge.energy.crm.organization.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Opt;
import com.kge.energy.crm.common.util.UserInfoContextUtils;
import com.kge.energy.crm.organization.req.OrgReq;
import com.kge.energy.crm.organization.resp.OrgDictResp;
import com.kge.energy.crm.organization.resp.OrgResp;
import com.kge.energy.crm.repository.dao.BOrganizationDao;
import com.kge.energy.crm.repository.entityext.result.OrgDictResult;
import com.kge.energy.crm.repository.entityext.result.OrgResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrgService {

    private final BOrganizationDao organizationDao;

    /**
     * @description 获取服务商
     * @author tangchenghui
     * @date 2024/7/26 17:56
     */
    public List<OrgResp> getCompanyList(OrgReq orgReq) {
        List<OrgResult> list1 = new ArrayList<>();
        List<OrgResult> list2 = new ArrayList<>();
        List<OrgResult> list3 = new ArrayList<>();
        List<OrgResult> list4 = new ArrayList<>();
        List<OrgResult> list5 = new ArrayList<>();

        List<OrgResp> list = new ArrayList<OrgResp>();
        list.add(new OrgResp(1, "电建服务", list1));
        list.add(new OrgResp(3, "工程咨询设计", list3));
        list.add(new OrgResp(4, "科技服务和智慧能源", list4));
        list.add(new OrgResp(5, "物业运营", list5));
        list.add(new OrgResp(2, "城建服务", list2));

        List<OrgResult> orgResultList = organizationDao.getCompanyList(UserInfoContextUtils.getCurrentTenantId());
        orgResultList.forEach(org -> {
            Opt.ofBlankAble(org.getType()).ifPresent(type -> {
                org.setTypeList(new ArrayList<>());
                org.getTypeList().addAll(List.of(type.split(",")));
            });

            if (org.getServiceType() == 1) {
                list1.add(org);
            } else if (org.getServiceType() == 2) {
                list2.add(org);
            } else if (org.getServiceType() == 3) {
                list3.add(org);
            } else if (org.getServiceType() == 4) {
                list4.add(org);
            } else if (org.getServiceType() == 5) {
                list5.add(org);
            }
        });

        return list;
    }

    public List<OrgDictResp> orgDictList() {

        List<OrgDictResult> orgDictResults = organizationDao.getOrgDictList(UserInfoContextUtils.getCurrentTenantId());

        return BeanUtil.copyToList(orgDictResults, OrgDictResp.class);
    }
}
