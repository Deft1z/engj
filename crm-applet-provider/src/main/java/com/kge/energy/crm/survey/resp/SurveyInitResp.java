/**
  * Copyright 2024 json.cn 
  */
package com.kge.energy.crm.survey.resp;
import com.kge.energy.crm.common.button.resp.BaseButton;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@Schema(description = "调查表单响应对象")
public class SurveyInitResp {

    @Schema(description = "提交标识：true-提交，false-临时保存")
    private Boolean submitFlag = false;

    @Schema(description = "页面可用操作按钮")
    private List<BaseButton> buttons;

    @Schema(description = "主键id")
    private Integer recordId;

    @Schema(description = "调查表单Id")
    private Integer surveyId;

    @Schema(description = "调查表单编码")
    private String surveyCode;

    @Schema(description = "调查表单名称")
    private String surveyName;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "调查表单项")
    private List<SurveyItem> surveyItems;

    @Data
    public static class SurveyItem{
        @Schema(description = "主键id")
        private Integer id;

        @Schema(description = "调查表单id")
        private Integer surveyId;

        @Schema(description = "表单项名称")
        private String itemName;

        @Schema(description = "表单项类型：title-标题，text-文本，radio-单选，checkbox-多选，select-下拉框，date-日期，time-时间，datetime-日期时间，number-数字，file-附件")
        private String itemType;

        @Schema(description = "是否必填：false-否，true-是")
        private Boolean required;

        @Schema(description = "父级id")
        private Integer parentId;

        @Schema(description = "优先级")
        private Integer priority;

        @Schema(description = "可填写人：all-所有人, promoter-发起人, invitee-受邀请人")
        private String fillBy;

        @Schema(description = "填写值")
        private String fillVal;

        @Schema(description = "填写选项")
        private List<SurveyItemOption> fillOptions;

        @Schema(description = "调查表单子项")
        private List<SurveyItem> subItems;
    }

    @Data
    public static class SurveyItemOption{
        @Schema(description = "主键id")
        private Integer id;

        @Schema(description = "调查表单项id")
        private Integer itemId;

        @Schema(description = "表单项值")
        private String itemVal;

        @Schema(description = "优先级")
        private Integer priority;
    }

}