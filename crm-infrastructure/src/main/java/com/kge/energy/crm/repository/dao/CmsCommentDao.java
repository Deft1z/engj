package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.CmsCommentMapper;
import com.kge.energy.crm.repository.entity.CmsComment;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 评论(CmsComment)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class CmsCommentDao extends ServiceImpl<CmsCommentMapper, CmsComment> {

    private final CmsCommentMapper mapper;

}

