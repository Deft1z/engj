package com.kge.energy.crm.repository.dao;

import com.kge.energy.crm.repository.mapper.RUserLikeCommentMapper;
import com.kge.energy.crm.repository.entity.RUserLikeComment;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 评论点赞(RUserLikeComment)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class RUserLikeCommentDao extends ServiceImpl<RUserLikeCommentMapper, RUserLikeComment> {

    private final RUserLikeCommentMapper mapper;

}

