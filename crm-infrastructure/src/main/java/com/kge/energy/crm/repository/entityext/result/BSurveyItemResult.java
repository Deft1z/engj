package com.kge.energy.crm.repository.entityext.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 调查表单项表(BSurveyItem)响应对象
 *
 * @author zhengwenke
 * @since 2024-10-30 09:27:34
 */
@Data
@Accessors(chain = true)
@Schema(description = "调查表单项表响应对象")
public class BSurveyItemResult {

    @Schema(description = "主键id")
    private Integer id;

    @Schema(description = "调查表单id")
    private Integer surveyId;

    @Schema(description = "表单项code")
    private String itemCode;

    @Schema(description = "表单项名称")
    private String itemName;

    @Schema(description = "表单项类型：title-标题，text-文本，longtext-长文本，radio-单选，checkbox-多选，select-下拉框，date-日期，time-时间，datetime-日期时间，number-数字，stars-星级，file-附件")
    private String itemType;

    @Schema(description = "是否必填：false-否，true-是")
    private Boolean required;

    @Schema(description = "父级id")
    private Integer parentId;

    @Schema(description = "优先级")
    private Integer priority;

    @Schema(description = "可填写人：all-所有人, promoter-发起人, invitee-受邀请人")
    private String fillBy;

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

    @Schema(description = "填写选项")
    private List<BSurveyItemOptionResult> fillOptions;

    @Schema(description = "调查表单子项")
    private List<BSurveyItemResult> subItems;

}



