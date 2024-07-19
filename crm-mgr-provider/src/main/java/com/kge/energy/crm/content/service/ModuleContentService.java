package com.kge.energy.crm.content.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kge.energy.crm.repository.dao.ModuleContentDao;
import com.kge.energy.crm.repository.entity.CmsBlockContent;
import com.kge.energy.crm.repository.entityext.param.ModuleContentParam;
import com.kge.energy.crm.repository.entityext.result.CmsBlockContentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModuleContentService {
    private final ModuleContentDao dao;

    public IPage<CmsBlockContentType> getPage(ModuleContentParam param) {
        return dao.getPage(param);
    }

    public boolean add(CmsBlockContent content) {
        return dao.add(content);
    }

    public boolean edit(CmsBlockContent content) {
        return dao.edit(content);
    }

    public boolean delete(List<Integer> ids) {
        return dao.delete(ids);
    }
}
