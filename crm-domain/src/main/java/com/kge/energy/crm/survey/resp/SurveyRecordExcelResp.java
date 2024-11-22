package com.kge.energy.crm.survey.resp;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.kge.energy.crm.easyexcel.DictConvert;
import com.kge.energy.crm.easyexcel.DictFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 调查表单 Excel 导出对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = false)
@ContentRowHeight(100)
public class SurveyRecordExcelResp {

    @ExcelProperty("项目编号")
    private String surveyObjCode;

    @ExcelProperty("项目名称")
    private String surveyObjName;

    @ExcelProperty("客户名称")
    private String clientName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelProperty("创建时间")
    @ColumnWidth(20)
    private LocalDateTime createTime;

    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat("survey-status")
    private Integer status;

    @ExcelIgnore
    @ExcelProperty("评价链接")
    private String shareUrl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelProperty("二维码有效期")
    private LocalDateTime shareExpireAt;

    @ExcelProperty(value = "评价二维码")
    @ColumnWidth(20)
    private byte[] shareQrCode;

}
