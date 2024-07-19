package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.repository.entity.CmsBlockContent;
import com.kge.energy.crm.repository.entityext.param.ModuleContentParam;
import com.kge.energy.crm.repository.entityext.result.CmsBlockContentType;
import com.kge.energy.crm.repository.mapper.CmsBlockContentMapper;
import com.kge.energy.crm.repository.mapper.CmsBlockContentTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ModuleContentDao {
    private final CmsBlockContentTypeMapper mapper;
    private final CmsBlockContentMapper contentMapper;

    public IPage<CmsBlockContentType> getPage(ModuleContentParam param) {
        Page<CmsBlockContentType> page = new Page<>(param.getCurrentPage(), param.getPageSize());
        return mapper.selectPage(page, param);
    }

    public boolean add(CmsBlockContent content) {
        content.setFlag(1);
        return contentMapper.insert(content) > 0;
    }

    public boolean edit(CmsBlockContent content) {
        return contentMapper.updateById(content) > 0;
    }

    public boolean delete(List<Integer> ids) {
        return contentMapper.deleteBatchIds(ids) > 0;
    }
}
