package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.CmsBlockMapper;
import com.kge.energy.crm.repository.entity.CmsBlock;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 版块(CmsBlock)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class CmsBlockDao extends ServiceImpl<CmsBlockMapper, CmsBlock> {

    private final CmsBlockMapper mapper;

}

