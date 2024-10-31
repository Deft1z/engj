package com.kge.energy.crm.repository.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 调查表单表(BSurvey)实体类
 *
 * @author zhengwenke
 * @since 2024-10-30 09:27:34
 */
@TableName(value = "b_survey")
@Data
@Accessors(chain = true)
public class BSurvey {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    @TableField(value = "id")
    private Integer id;

    /**
     * 表单编码
     */
    @TableField(value = "survey_code")
    private String surveyCode;

    /**
     * 调查表单名称
     */
    @TableField(value = "survey_name")
    private String surveyName;

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



