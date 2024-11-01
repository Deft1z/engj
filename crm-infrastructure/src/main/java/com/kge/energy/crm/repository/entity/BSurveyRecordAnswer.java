package com.kge.energy.crm.repository.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 调查表单记录填写表(BSurveyRecordAnswer)实体类
 *
 * @author zhengwenke
 * @since 2024-10-30 09:27:36
 */
@TableName(value = "b_survey_record_answer")
@Data
@Accessors(chain = true)
public class BSurveyRecordAnswer {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    @TableField(value = "id")
    private Integer id;

    /**
     * 发起人id
     */
    @TableField(value = "promoter_id")
    private Integer promoterId;

    /**
     * 受邀请人id
     */
    @TableField(value = "invitee_id")
    private Integer inviteeId;

    /**
     * 调查表单记录id
     */
    @TableField(value = "survey_record_id")
    private Integer surveyRecordId;

    /**
     * 调查表单名称
     */
    @TableField(value = "survey_name")
    private String surveyName;

    /**
     * 受邀请人填写的表单内容
     */
    @TableField(value = "fill_json")
    private String fillJson;

    /**
     * 0 未提交 1 待评价 2 已评价
     */
    @TableField(value = "status")
    private Integer status;

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



