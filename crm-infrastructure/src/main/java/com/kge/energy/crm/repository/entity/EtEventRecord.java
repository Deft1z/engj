package com.kge.energy.crm.repository.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 埋点事件记录表(EtEventRecord)实体类
 *
 * @author wangjihua
 * @since 2024-10-11 09:19:32
 */
@Data
@Accessors(chain = true)
public class EtEventRecord {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 事件ID
     */
    private Integer eventId;

    /**
     * 访问页面名称
     */
    private String viewPageName;

    /**
     * 访问页面地址
     */
    private String viewPageUrl;

    /**
     * 来源页面名称
     */
    private String sourcePageName;

    /**
     * 来源页面地址
     */
    private String sourcePageUrl;

    /**
     * 事件发生时间
     */
    private LocalDateTime eventTime;

    /**
     * 事件时长，单位毫秒
     */
    private Integer eventDuration;

    /**
     * 事件属性
     */
    private String eventProperty;

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

    /**
     * 租户id
     */
    private Integer tenantId;
}

