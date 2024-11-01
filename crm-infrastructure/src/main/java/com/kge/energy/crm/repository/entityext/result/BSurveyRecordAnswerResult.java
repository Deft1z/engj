package com.kge.energy.crm.repository.entityext.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 调查表单记录填写表(BSurveyRecordAnswer)响应对象
 *
 * @author zhengwenke
 * @since 2024-10-30 09:27:36
 */
@Data
@Accessors(chain = true)
@Schema(description = "调查表单记录填写表响应对象")
public class BSurveyRecordAnswerResult {

    @Schema(description = "主键id")
    private Integer id;

    @Schema(description = "发起人id")
    private Integer promoterId;

    @Schema(description = "受邀请人id")
    private Integer inviteeId;

    @Schema(description = "调查表单记录id")
    private Integer surveyRecordId;

    @Schema(description = "调查表单名称")
    private String surveyName;

    @Schema(description = "受邀请人填写的表单内容")
    private String fillJson;

    @Schema(description = "0 未提交 1 待评价 2 已评价")
    private Integer status;

    @Schema(description = "创建人ID")
    private Integer createUserId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人ID")
    private Integer modifyUserId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private LocalDateTime modifyTime;

    @Schema(description = "数据状态：-1-删除，1-正常")
    private Integer flag;

    @Schema(description = "租户id")
    private Integer tenantId;

}



