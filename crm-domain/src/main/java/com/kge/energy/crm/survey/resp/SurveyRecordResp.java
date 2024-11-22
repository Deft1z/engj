package com.kge.energy.crm.survey.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 调查表单响应对象
 *
 * @author zhengwenke
 * @since 2024-10-30 15:56:16
 */
@Data
@Accessors(chain = true)
@Schema(description = "调查表单记录表响应对象")
public class SurveyRecordResp {

    @Schema(description = "主键id")
    private Integer id;

    @Schema(description = "调查表单id")
    private Integer surveyId;

    @Schema(description = "调查表名称")
    private String surveyName;

    @Schema(description = "调查对象（项目、合同、设备、工单...）编码")
    private String surveyObjCode;

    @Schema(description = "调查对象（项目、合同、设备、工单...）名称")
    private String surveyObjName;

    @JsonIgnore
    @Schema(description = "分享评价链接")
    private String shareUrl;

    @JsonIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "分享链接过期时间")
    private LocalDateTime shareExpireAt;

    @Schema(description = "0 未提交 1 待评价 2 已完成")
    private Integer status;

    @Schema(description = "客户名称")
    private String clientName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间(发起时间)")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间(完成时间)")
    private LocalDateTime modifyTime;

}



