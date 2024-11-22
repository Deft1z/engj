package com.kge.energy.crm.survey.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 调查表单表(BSurvey)响应对象
 *
 * @author zhengwenke
 * @since 2024-10-30 09:27:34
 */
@Data
@Accessors(chain = true)
@Schema(description = "调查表单表响应对象")
public class SurveyResult {

    @Schema(description = "主键id")
    private Integer id;

    @Schema(description = "表单编码")
    private String surveyCode;

    @Schema(description = "调查表单名称")
    private String surveyName;

    @Schema(description = "备注")
    private String remark;


}



