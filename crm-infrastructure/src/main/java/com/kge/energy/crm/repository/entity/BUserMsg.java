package com.kge.energy.crm.repository.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用户消息记录表(BUserMsg)实体类
 *
 * @author wangjihua
 * @since 2024-08-09 09:06:08
 */
@Data
@Accessors(chain = true)
public class BUserMsg {

    /**
     * 用户消息主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 接收消息的用户id
     */
    private Integer userId;

    /**
     * 接收消息的用户姓名
     */
    private String realname;

    /**
     * 系统类型：applet、mgr
     */
    private String systemType;

    /**
     * 消息业务id
     */
    private String msgBizId;

    /**
     * 消息业务类型， 0 告警信息 1 工单通知 2 项目合同 3 投诉处理
     */
    private Integer msgBizType;

    /**
     * 地址类型：system、file、bapp、link
     */
    private String pathType;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 已读标识，false 未读 true 已读
     */
    @TableField(value = "is_read")
    private Boolean isRead;

    /**
     * 创建时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

