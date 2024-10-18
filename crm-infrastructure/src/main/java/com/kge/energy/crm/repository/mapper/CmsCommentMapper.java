package com.kge.energy.crm.repository.mapper;

import com.kge.energy.crm.repository.entity.CmsComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kge.energy.crm.repository.entityext.param.CmsCommentParam;
import com.kge.energy.crm.repository.entityext.result.AppletCommentResult;
import com.kge.energy.crm.repository.entityext.result.CmsCommentResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论(CmsComment)表数据库接口层
 */
public interface CmsCommentMapper extends BaseMapper<CmsComment> {

    List<AppletCommentResult> getAllCommentsByUserId(Integer userId);

    List<CmsCommentResult> getCmsCommentList(@Param("param") CmsCommentParam param);
}

