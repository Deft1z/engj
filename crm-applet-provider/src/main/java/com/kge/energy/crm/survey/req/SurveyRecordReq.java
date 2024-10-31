package com.kge.energy.crm.survey.req;

import com.kge.energy.crm.common.page.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 调查表单查询对象
 *
 * @author zhengwenke
 * @since 2024-10-30 15:56:16
 */
@Data
@Accessors(chain = true)
@Schema(description = "调查表单记录表响应对象")
public class SurveyRecordReq extends PageReq {

    @Schema(description = "调查对象（项目、合同、设备、工单...）编码")
    private String surveyObjCode;

    @Schema(description = "调查对象（项目、合同、设备、工单...）名称")
    private String surveyObjName;

    @Schema(description = "0 未提交 1 待评价 2 已评价 3 已完成")
    private Integer status;


}



