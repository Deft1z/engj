package com.kge.energy.crm.repository.dao;

import cn.hutool.core.lang.Opt;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kge.energy.crm.repository.entity.BOpenidShare;
import com.kge.energy.crm.repository.entityext.param.CmsCommentParam;
import com.kge.energy.crm.repository.entityext.result.AppletCommentResult;
import com.kge.energy.crm.repository.entityext.result.CmsCommentResult;
import com.kge.energy.crm.repository.mapper.CmsCommentMapper;
import com.kge.energy.crm.repository.entity.CmsComment;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * 评论(CmsComment)表数据库访问层
 */
@Repository
@RequiredArgsConstructor
public class CmsCommentDao extends ServiceImpl<CmsCommentMapper, CmsComment> {

    private final CmsCommentMapper mapper;

    public List<AppletCommentResult> getAllCommentsByUserId(Integer userId){
        return mapper.getAllCommentsByUserId(userId);
    }

    public boolean updateLikeNumber(Integer commentId, Integer likeNumber){
        LambdaUpdateWrapper<CmsComment> wrapper = Wrappers.<CmsComment>update().lambda()
                .set(CmsComment::getLikeNumber, likeNumber)
                .eq(CmsComment::getCommentId, commentId);
        int resultInt = mapper.update(wrapper);
        return resultInt != 0;
    }

    public boolean thumbsUp(Integer commentId) {
        CmsComment cmsComment = mapper.selectById(commentId);
        LambdaUpdateWrapper<CmsComment> wrapper = Wrappers.<CmsComment>update().lambda()
                .set(CmsComment::getLikeNumber, Opt.ofNullable(cmsComment.getLikeNumber()).orElse(0) + 1)
                .eq(CmsComment::getCommentId, commentId);
        int resultInt = mapper.update(wrapper);
        return resultInt != 0;
    }

    public boolean reduceLikeNumber(Integer commentId) {
        CmsComment cmsComment = mapper.selectById(commentId);
        LambdaUpdateWrapper<CmsComment> wrapper = Wrappers.<CmsComment>update().lambda()
                .set(CmsComment::getLikeNumber, Opt.ofNullable(cmsComment.getLikeNumber()).orElse(0) - 1)
                .eq(CmsComment::getCommentId, commentId)
                .gt(CmsComment::getLikeNumber, 0);
        int resultInt = mapper.update(wrapper);
        return resultInt != 0;
    }

    public List<CmsCommentResult> getCmsCommentList(CmsCommentParam param) {
        return mapper.getCmsCommentList(param);
    }

}

