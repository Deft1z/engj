package com.kge.energy.crm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kge.energy.crm.repository.entity.CmsBlock;
import org.apache.ibatis.annotations.Mapper;

/**
 * 版块(CmsBlock)表数据库接口层
 */
@Mapper
public interface CmsBlockMapper extends BaseMapper<CmsBlock> {}

