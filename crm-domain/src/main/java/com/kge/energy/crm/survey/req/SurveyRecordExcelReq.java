package com.kge.energy.crm.survey.req;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 调查表单 Excel 导入对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = false) // 设置 chain = false，避免用户导入有问题
public class SurveyRecordExcelReq {

    @ExcelProperty("项目名称")
    private String projectName;

    @ExcelProperty("项目编号")
    private String projectNum;

    @ExcelProperty("项目类型")
    private String projectType;

    @ExcelProperty("服务单位")
    private String serviceUnit;

    @ExcelProperty("服务地址")
    private String serviceAddr;

    @ExcelProperty("回访人员")
    private String returnVisitor;

    @ExcelProperty("回访人电话")
    private String returnPhone;

    @ExcelProperty("客户名称")
    private String clientName;

    @ExcelProperty("客户手机")
    private String clientPhone;

    @ExcelProperty("备注")
    private String remark;

}
