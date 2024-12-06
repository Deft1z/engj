package com.kge.energy.crm.repository.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.repository.entity.BOrganization;
import com.kge.energy.crm.repository.entity.BOrganizationDetail;
import com.kge.energy.crm.repository.entityext.param.CompanyParam;
import com.kge.energy.crm.repository.mapper.BOrganizationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CompanyDao {
    private final BOrganizationMapper mapper;
    public Page<BOrganization> getPage(CompanyParam param) {
        LambdaQueryWrapper<BOrganization> wrapper = Wrappers.<BOrganization>lambdaQuery()
                .like(StrUtil.isNotBlank(param.getName()), BOrganization::getName, param.getName());
        return mapper.selectPage(new Page<>(param.getCurrentPage(), param.getPageSize()), wrapper);
    }

    public boolean edit(CompanyParam param) {
        return mapper.updateCompany(param);
    }

    public boolean editCover(CompanyParam param) {
        return mapper.updateCompanyCover(param);
    }
}
