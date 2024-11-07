package com.kge.energy.crm.common.button.helper;

import com.kge.energy.crm.common.button.enums.SurveyButtonEnum;
import com.kge.energy.crm.common.button.resp.BaseButton;
import com.kge.energy.crm.repository.entity.BSurveyRecord;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 调查表单按钮 Helper 类
 *
 * @author zhengwenke
 */
public class SurveyButtonHelper extends AbstractButtonHelper {

    public static List<BaseButton> getButtons(BSurveyRecord surveyRecord, Integer operatorId) {
        if (surveyRecord == null) {
            surveyRecord = new BSurveyRecord().setCreateUserId(operatorId);
        }
        List<BaseButton> buttons = Collections.emptyList();
        //是否是调查发起人
        boolean isPromoter = operatorId.equals(surveyRecord.getCreateUserId());
        if (isPromoter) {
            //status: 0 未提交 1 待评价 2 已完成
            //调查发起人
            if (surveyRecord.getStatus() == null) {
                buttons = AbstractButtonHelper.createdButtonList(Arrays.asList(SurveyButtonEnum.SURVEY_SAVE, SurveyButtonEnum.SURVEY_SUBMIT));
            } else if (surveyRecord.getStatus().equals(0)) {
                buttons = AbstractButtonHelper.createdButtonList(Arrays.asList(SurveyButtonEnum.SURVEY_SAVE, SurveyButtonEnum.SURVEY_SUBMIT, SurveyButtonEnum.SURVEY_DELETE));
            } else if (surveyRecord.getStatus().equals(2)) {
                buttons = AbstractButtonHelper.createdButtonList(Arrays.asList(SurveyButtonEnum.SURVEY_SHARE, SurveyButtonEnum.SURVEY_FINISH));
            } else {
                buttons = AbstractButtonHelper.createdButtonList(Arrays.asList(SurveyButtonEnum.SURVEY_SHARE));
            }
        } else {
            //调查受邀请人
            if (surveyRecord.getStatus().equals(1)) {
                buttons = AbstractButtonHelper.createdButtonList(Arrays.asList(SurveyButtonEnum.ANSWER_SAVE, SurveyButtonEnum.ANSWER_SUBMIT));
            }
        }
        return buttons;
    }

}
