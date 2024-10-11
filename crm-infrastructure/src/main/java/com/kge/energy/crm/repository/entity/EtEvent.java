package com.kge.energy.crm.repository.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 埋点事件表(EtEvent)实体类
 *
 * @author wangjihua
 * @since 2024-10-11 09:19:32
 */
@Data
@Accessors(chain = true)
public class EtEvent {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 系统类型：applet、mgr
     */
    private String systemType;

    /**
     * 事件键名
     */
    private String eventKey;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 事件类型（1-启动，2-登录，3-浏览，4-点击，5-搜索，6-分享）
     */
    private Integer eventType;

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

