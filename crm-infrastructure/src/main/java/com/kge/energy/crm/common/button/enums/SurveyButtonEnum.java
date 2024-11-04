package com.kge.energy.crm.common.button.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhengwenke
 * @date 2024/11/4 9:16
 */
@Getter
@AllArgsConstructor
public enum SurveyButtonEnum implements ButtonEnum {

    SURVEY_SAVE("survey_save", "保存", true, null),
    SURVEY_SUBMIT("survey_submit", "提交", true, null),
    SURVEY_DELETE("survey_delete", "删除", true, null),
    SURVEY_SHARE("survey_share", "分享", true, null),
    ANSWER_SAVE("answer_save", "保存", true, null),
    ANSWER_SUBMIT("answer_submit", "提交", true, null),
    SURVEY_FINISH("survey_finish", "完成", true, null),
    ;

    private final String code;

    private final String name;

    private final Boolean enabled;

    private final String hint;
}
