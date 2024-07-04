package com.kge.energy.crm.repository.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 评论点赞(RUserLikeComment)实体类
 *
 * @author wangjihua
 * @since 2024-07-03 20:38:24
 */
@Data
@Accessors(chain = true)
public class RUserLikeComment {

    /**
     * 评论点赞
     */
    @TableId(type = IdType.AUTO)
    private Integer userLikeCommentId; 

    /**
     * 评论唯一标识
     */
    private Integer commentId; 

    /**
     * 点赞用户
     */
    private Integer userId; 

    /**
     * 点赞状态，1：点赞，-1：取消点赞
     */
    private Integer flag; 

    /**
     * 创建用户
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer createUserId; 

    /**
     * 创建时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime; 

    /**
     * 修改用户
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer modifyUserId; 

    /**
     * 修改时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime modifyTime; 
}

