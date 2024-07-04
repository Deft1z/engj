package com.kge.energy.crm.repository.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.*;

/**
 * cms_block_content 板块内容(CmsBlockContent)实体类
 *
 * @author wangjihua
 * @since 2024-07-03 20:38:23
 */
@Data
@Accessors(chain = true)
public class CmsBlockContent {

    /**
     * 模块内容
     */
    @TableId(type = IdType.AUTO)
    private Integer blockContentId; 

    /**
     * cms_block主键
     */
    private Integer blockId; 

    /**
     * b_app主键,可为空
     */
    private Integer appId; 

    /**
     * 标题
     */
    private String title; 

    /**
     * 文章内容
     */
    private String desc; 

    /**
     * 图片链接
     */
    private String imageUrl; 

    /**
     * 停用
     */
    private Integer pageUrl; 

    /**
     * H5介绍页面链接
     */
    private Integer pageFile; 

    /**
     * 软删除标识
     */
    private Integer flag; 

    /**
     * 创建用户ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer createUserId; 

    /**
     * 创建时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime; 

    /**
     * 修改用户ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer modifyUserId; 

    /**
     * 修改时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime modifyTime; 

    /**
     * 备注
     */
    private String remark; 
}

