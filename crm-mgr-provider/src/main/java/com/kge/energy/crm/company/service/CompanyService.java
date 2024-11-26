package com.kge.energy.crm.company.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Opt;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.company.resp.CompanyResp;
import com.kge.energy.crm.repository.dao.BOrganizationDetailDao;
import com.kge.energy.crm.repository.dao.CompanyDao;
import com.kge.energy.crm.repository.entity.BOrganization;
import com.kge.energy.crm.repository.entity.BOrganizationDetail;
import com.kge.energy.crm.repository.entityext.param.CompanyParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyDao dao;
    private final BOrganizationDetailDao bOrganizationDetailDao;

    public Page<CompanyResp> getPage(CompanyParam param) {
        Page<BOrganization> page = dao.getPage(param);
        List<BOrganization> records = page.getRecords();
        // BOrganization列表转换为CompanyResp列表
        List<CompanyResp> companyList = BeanUtil.copyToList(records, CompanyResp.class);
        HashMap<String, String> mapping = new HashMap<>();
        mapping.put("label", "tag");
        for (CompanyResp companyResp : companyList) {
            Integer organizationId = companyResp.getOrganizationId();
            BOrganizationDetail organizationDetail = bOrganizationDetailDao.getById(organizationId);
            BeanUtil.copyProperties(organizationDetail, companyResp, CopyOptions.create().setFieldMapping(mapping));
            Opt.ofNullable(organizationDetail).ifPresent(p -> {
                switch (organizationDetail.getServiceType()) {
                    case 1 -> companyResp.setServiceTypeString("电建服务");
                    case 2 -> companyResp.setServiceTypeString("城建服务");
                    case 3 -> companyResp.setServiceTypeString("工程咨询设计");
                    case 4 -> companyResp.setServiceTypeString("科技服务和智慧能源");
                    case 5 -> companyResp.setServiceTypeString("物业运营");
                    default -> companyResp.setServiceTypeString("");
                }
            });
        }
        Page<CompanyResp> res = new Page<>();
        // 拷贝分页属性
        BeanUtil.copyProperties(page, res, "records");
        res.setRecords(companyList);
        return res;
    }

    public boolean edit(CompanyParam param) {
        return dao.edit(param);
    }

    public boolean editCover(CompanyParam param) {
        return dao.editCover(param);
    }
}
