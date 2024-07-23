package com.kge.energy.crm.company.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.company.resp.CompanyResp;
import com.kge.energy.crm.repository.dao.CompanyDao;
import com.kge.energy.crm.repository.entity.BOrganization;
import com.kge.energy.crm.repository.entityext.param.CompanyParam;
import com.kge.energy.crm.repository.entityext.result.OrganizationParameter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyDao dao;

    public Page<CompanyResp> getPage(CompanyParam param) {
        Page<BOrganization> page = dao.getPage(param);
        List<BOrganization> records = page.getRecords();
        // BOrganization列表转换为CompanyResp列表
        List<CompanyResp> companyList = BeanUtil.copyToList(records, CompanyResp.class);

        HashMap<String, String> mapping = new HashMap<>();
        mapping.put("type", "tag");

        for (int i = 0; i < records.size(); i++) {
            OrganizationParameter parameter = records.get(i).getParameter();
            CompanyResp item = companyList.get(i);
            // 拷贝parameter中的属性到CompanyResp中
            BeanUtil.copyProperties(parameter, item, CopyOptions.create().setFieldMapping(mapping));

            switch (parameter.getServiceType()) {
                case 1 -> item.setServiceTypeString("电建服务");
                case 2 -> item.setServiceTypeString("城建服务");
                case 3 -> item.setServiceTypeString("工程咨询设计");
                case 4 -> item.setServiceTypeString("科技服务和智慧能源");
                case 5 -> item.setServiceTypeString("物业运营");
                default -> item.setServiceTypeString("");
            }

            if (StrUtil.isNotBlank(parameter.getType())) {
                item.setTagList(parameter.getType().split(","));
            }
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
