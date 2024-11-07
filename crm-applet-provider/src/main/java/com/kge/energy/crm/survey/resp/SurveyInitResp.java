/**
  * Copyright 2024 json.cn 
  */
package com.kge.energy.crm.survey.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kge.energy.crm.common.button.resp.BaseButton;
import com.kge.energy.crm.repository.entity.BSurveyRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
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

    @Schema(description = "分享二维码")
    private ShareQrCode shareQrCode;

    @Data
    public static class SurveyItem{
        @Schema(description = "主键id")
        private Integer id;

        @Schema(description = "调查表单id")
        private Integer surveyId;

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

        @Schema(description = "是否可填写：true-可填写，false-不可填写")
        private Boolean fillEnabled;

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

    @Data
    public static class ShareQrCode{

        @Schema(description = "分享二维码Base64")
        private String base64Image;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Schema(description = "过期时间")
        private LocalDateTime expireAt;

    }

    public static void setLockedSurveyItem(BSurveyRecord surveyRecord, Integer operatorId, List<SurveyInitResp.SurveyItem> surveyItems) {
        //是否是调查发起人
        boolean isPromoter = surveyRecord == null? true : operatorId.equals(surveyRecord.getCreateUserId());
        for (SurveyInitResp.SurveyItem surveyItem : surveyItems) {
            if ((isPromoter && surveyItem.getFillBy().equals("invitee")) || (!isPromoter && surveyItem.getFillBy().equals("promoter"))) {
                surveyItem.setFillEnabled(false);
            } else {
                surveyItem.setFillEnabled(true);
            }
            if (!surveyItem.getSubItems().isEmpty()) {
                setLockedSurveyItem(surveyRecord, operatorId, surveyItem.getSubItems());
            }
        }
    }

}