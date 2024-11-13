package com.kge.energy.crm.repository.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 新闻记录表(BNewsRecord)实体类
 *
 * @author wangjihua
 * @since 2024-11-13 11:52:17
 */
@Data
@Accessors(chain = true)
public class BNewsRecord {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 新闻类型ID
     */
    private Integer typeId;

    /**
     * 新闻标题
     */
    private String title;

    /**
     * 新闻内容
     */
    private String content;

    /**
     * 新闻附件
     */
    private String attachment;

    /**
     * 新闻发布日期
     */
    private LocalDateTime publishDate;

    /**
     * 新闻源地址
     */
    private String sourceUrl;

    /**
     * 数据状态：-1-删除，1-正常
     */
    private Integer flag;

    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer createUserId;

    /**
     * 创建时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime;

    /**
     * 更新人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer modifyUserId;

    /**
     * 更新时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime modifyTime;
}

