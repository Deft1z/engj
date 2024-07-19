package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kge.energy.crm.repository.entityext.param.ModuleContentParam;
import com.kge.energy.crm.repository.entityext.result.CmsBlockContentType;
import org.apache.ibatis.annotations.Param;

public interface CmsBlockContentTypeMapper extends BaseMapper<CmsBlockContentType> {
    IPage<CmsBlockContentType> selectPage(Page<CmsBlockContentType> page, @Param("param") ModuleContentParam param);
}
