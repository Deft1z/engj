package com.kge.energy.crm.repository.mapper;

import com.kge.energy.crm.repository.entity.RUserLikeComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论点赞(RUserLikeComment)表数据库接口层
 */
public interface RUserLikeCommentMapper extends BaseMapper<RUserLikeComment> {

    List<RUserLikeComment> findThumbsUp(@Param("param") RUserLikeComment userLikeComment);

    int thumbsUp2(@Param("param") RUserLikeComment userLikeComment);
}

