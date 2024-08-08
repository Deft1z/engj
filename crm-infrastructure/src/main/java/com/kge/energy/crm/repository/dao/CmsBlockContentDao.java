package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.CmsBlockContentMapper;
import com.kge.energy.crm.repository.entity.CmsBlockContent;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * cms_block_content 板块内容(CmsBlockContent)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class CmsBlockContentDao extends ServiceImpl<CmsBlockContentMapper, CmsBlockContent> {

    private final CmsBlockContentMapper mapper;

}

