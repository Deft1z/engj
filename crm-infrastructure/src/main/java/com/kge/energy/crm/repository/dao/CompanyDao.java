package com.kge.energy.crm.repository.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.repository.entity.BOrganization;
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
        LambdaUpdateWrapper<BOrganization> wrapper = Wrappers.<BOrganization>lambdaUpdate()
                .set(StrUtil.isNotBlank(param.getName()), BOrganization::getName, param.getName())
                .set(StrUtil.isNotBlank(param.getRemark()), BOrganization::getRemark, param.getRemark())
                .setSql("parameter = JSON_SET(parameter, '$.fullName', {0})", Optional.ofNullable(param.getFullName()).orElse(""))
                .setSql("parameter = JSON_SET(parameter, '$.serviceType', {0})", Optional.ofNullable(param.getServiceType()).orElse(0))
                .eq(BOrganization::getOrganizationId, param.getOrganizationId());
        return mapper.update(wrapper) > 0;
    }

    public boolean editCover(CompanyParam param) {
        LambdaUpdateWrapper<BOrganization> wrapper = Wrappers.<BOrganization>lambdaUpdate()
                .setSql("parameter = JSON_SET(parameter, '$.filepath', {0})", Optional.ofNullable(param.getFilePath()).orElse(""))
                .eq(BOrganization::getOrganizationId, param.getOrganizationId());
        return mapper.update(wrapper) > 0;
    }
}
