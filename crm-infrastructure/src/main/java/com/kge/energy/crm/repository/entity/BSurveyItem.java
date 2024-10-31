package com.kge.energy.crm.repository.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 调查表单项表(BSurveyItem)实体类
 *
 * @author zhengwenke
 * @since 2024-10-30 09:27:34
 */
@TableName(value = "b_survey_item")
@Data
@Accessors(chain = true)
public class BSurveyItem {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    @TableField(value = "id")
    private Integer id;

    /**
     * 调查表单id
     */
    @TableField(value = "survey_id")
    private Integer surveyId;

    /**
     * 表单项名称
     */
    @TableField(value = "item_name")
    private String itemName;

    /**
     * 表单项类型：title-标题，text-文本，radio-单选，checkbox-多选，select-下拉框，date-日期，time-时间，datetime-日期时间，number-数字，file-附件
     */
    @TableField(value = "item_type")
    private String itemType;

    /**
     * 是否必填：false-否，true-是
     */
    @TableField(value = "required")
    private Boolean required;

    /**
     * 父级id
     */
    @TableField(value = "parent_id")
    private Integer parentId;

    /**
     * 优先级
     */
    @TableField(value = "priority")
    private Integer priority;

    /**
     * 可填写人：all-所有人, promoter-发起人, invitee-受邀请人
     */
    @TableField(value = "fill_by")
    private String fillBy;

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



