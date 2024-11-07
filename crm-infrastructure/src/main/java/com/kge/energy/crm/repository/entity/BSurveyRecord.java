package com.kge.energy.crm.repository.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 调查表单记录表(BSurveyRecord)实体类
 *
 * @author zhengwenke
 * @since 2024-10-30 15:56:16
 */
@TableName(value = "b_survey_record")
@Data
@Accessors(chain = true)
public class BSurveyRecord {

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
     * 调查表名称
     */
    @TableField(value = "survey_name")
    private String surveyName;

    /**
     * 发起人填写的表单内容
     */
    @TableField(value = "fill_json")
    private String fillJson;

    /**
     * 分享评价链接
     */
    @TableField(value = "share_url")
    private String shareUrl;

    /**
     * 分享链接过期时间
     */
    @TableField(value = "share_expire_at")
    private LocalDateTime shareExpireAt;

    /**
     * 0 未提交 1 待评价 2 已完成
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



