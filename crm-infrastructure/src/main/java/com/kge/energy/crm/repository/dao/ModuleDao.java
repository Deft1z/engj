package com.kge.energy.crm.repository.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.repository.entity.CmsBlock;
import com.kge.energy.crm.repository.entityext.param.ModuleParam;
import com.kge.energy.crm.repository.mapper.CmsBlockMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ModuleDao {
    private final CmsBlockMapper mapper;

    public Page<CmsBlock> getPage(ModuleParam param) {
        Page<CmsBlock> page = new Page<>(param.getCurrentPage(), param.getPageSize());
        LambdaQueryWrapper<CmsBlock> wrapper = Wrappers.<CmsBlock>lambdaQuery()
                .like(StrUtil.isNotBlank(param.getName()), CmsBlock::getName, param.getName())
                .orderByAsc(CmsBlock::getCode);
        return mapper.selectPage(page, wrapper);
    }

    public boolean hasSameCode(CmsBlock cmsBlock) {
        LambdaQueryWrapper<CmsBlock> wrapper = Wrappers.<CmsBlock>lambdaQuery()
                .ne(
                        Optional.ofNullable(cmsBlock.getBlockId())
                                .map(e -> e > 0)
                                .orElse(Boolean.FALSE),
                        CmsBlock::getBlockId, cmsBlock.getBlockId()
                )
                .eq(CmsBlock::getCode, cmsBlock.getCode());
        return mapper.selectCount(wrapper) > 0;
    }

    public boolean add(CmsBlock cmsBlock) {
        return mapper.insert(cmsBlock) > 0;
    }

    public boolean edit(CmsBlock cmsBlock) {
        return mapper.updateById(cmsBlock) > 0;
    }

    public boolean delete(List<Integer> ids) {
        return mapper.deleteBatchIds(ids) > 0;
    }
}
