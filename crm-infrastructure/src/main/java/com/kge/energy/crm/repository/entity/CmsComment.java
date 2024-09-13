package com.kge.energy.crm.repository.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 评论(CmsComment)实体类
 *
 * @author wangjihua
 * @since 2024-07-03 20:38:23
 */
@Data
@Accessors(chain = true)
public class CmsComment {

    /**
     * 评论唯一标识
     */
    @TableId(type = IdType.AUTO)
    private Integer commentId; 

    /**
     * 父级评论ID
     */
    private Integer parentCommentId; 

    /**
     * 提交评论的用户
     */
    private Integer createUserId;

    /**
     * 评论内容
     */
    private String content; 

    /**
     * 点赞数
     */
    private Integer likeNumber; 

    /**
     * 创建时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime;

    /**
     * 数据状态：-1-删除，1-正常
     */
    private Integer flag;

    /**
     * 业务数据id
     */
    private Integer bizDataId;

    /**
     * 业务类型:1-南综光伏项目 2-业务工单 3-投诉建议
     */
    private Integer bizType;
}

