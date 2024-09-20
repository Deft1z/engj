package com.kge.energy.crm.repository.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 业务功能消息配置表(CfBizFunctionMsg)实体类
 *
 * @author zhengwenke
 * @since 2024-09-19 10:30:28
 */
@TableName(value = "cf_biz_function_msg")
@Data
@Accessors(chain = true)
public class CfBizFunctionMsg {

    /**
     * 业务功能id
     */
    @TableField(value = "biz_function_id")
    private Integer bizFunctionId;

    /**
     * 消息渠道id
     */
    @TableField(value = "msg_channel_id")
    private Integer msgChannelId;

    /**
     * 黑名单,userIds
     */
    @TableField(value = "blacklist")
    private String blacklist;

    /**
     * 白名单,userIds
     */
    @TableField(value = "whitelist")
    private String whitelist;

    /**
     * 通知优先等级
     */
    @TableField(value = "priority")
    private Integer priority;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 创建人ID
     */
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    private Integer createUserId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime;

    /**
     * 更新人ID
     */
    @TableField(value = "modify_user_id", fill = FieldFill.INSERT_UPDATE)
    private Integer modifyUserId;

    /**
     * 更新时间
     */
    @TableField(value = "modify_time", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime modifyTime;

    /**
     * 数据状态：-1-删除，1-正常
     */
    @TableField(value = "flag")
    private Integer flag;

    /**
     * 租户id
     */
    @TableField(value = "tenant_id")
    private Integer tenantId;

}



