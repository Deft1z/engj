package com.kge.energy.crm.repository.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 调查表单项选值表(BSurveyItemOption)实体类
 *
 * @author zhengwenke
 * @since 2024-10-30 09:27:35
 */
@TableName(value = "b_survey_item_option")
@Data
@Accessors(chain = true)
public class BSurveyItemOption {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    @TableField(value = "id")
    private Integer id;

    /**
     * 调查表单项id
     */
    @TableField(value = "item_id")
    private Integer itemId;

    /**
     * 表单项值
     */
    @TableField(value = "item_val")
    private String itemVal;

    /**
     * 优先级
     */
    @TableField(value = "priority")
    private Integer priority;

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



