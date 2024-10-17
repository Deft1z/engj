package com.kge.energy.crm.repository.dao;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kge.energy.crm.repository.entity.RUserLikeComment;
import com.kge.energy.crm.repository.mapper.RUserLikeCommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


/**
 * 评论点赞(RUserLikeComment)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class RUserLikeCommentDao extends ServiceImpl<RUserLikeCommentMapper, RUserLikeComment> {

    private final RUserLikeCommentMapper mapper;

    public boolean findThumbsUp(RUserLikeComment userLikeComment) {
        return !mapper.findThumbsUp(userLikeComment).isEmpty();
    }

    public boolean findThumbsUp2(RUserLikeComment userLikeComment) {
        userLikeComment.setFlag(0);
        return !mapper.findThumbsUp(userLikeComment).isEmpty();
    }

    public boolean thumbsUp2(RUserLikeComment userLikeComment) {
        return mapper.thumbsUp2(userLikeComment) != 0;
    }

    public boolean cancelThumbsUp(RUserLikeComment userLikeComment) {
        LambdaUpdateWrapper<RUserLikeComment> wrapper = Wrappers.<RUserLikeComment>update().lambda()
                .set(RUserLikeComment::getFlag, 0)
                .eq(RUserLikeComment::getCommentId, userLikeComment.getCommentId())
                .eq(RUserLikeComment::getUserId, userLikeComment.getUserId())
                .eq(RUserLikeComment::getFlag, 1);
        return mapper.update(wrapper) != 0;
    }

}

