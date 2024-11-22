package com.kge.energy.crm.repository.entityext.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 调查表单记录表(BSurveyRecord)响应对象
 *
 * @author zhengwenke
 * @since 2024-10-30 15:56:16
 */
@Data
@Accessors(chain = true)
@Schema(description = "调查表单记录表响应对象")
public class BSurveyRecordResult {

    @Schema(description = "主键id")
    private Integer id;

    @Schema(description = "调查表单id")
    private Integer surveyId;

    @Schema(description = "调查表名称")
    private String surveyName;

    @Schema(description = "发起人填写的表单内容")
    private String fillJson;

    @Schema(description = "分享评价链接")
    private String shareUrl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "分享链接过期时间")
    private LocalDateTime shareExpireAt;

    @Schema(description = "0 未提交 1 待评价 2 已完成")
    private Integer status;

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

    @Schema(description = "调查对象（项目、合同、设备、工单...）编码")
    private String surveyObjCode;

    @Schema(description = "调查对象（项目、合同、设备、工单...）名称")
    private String surveyObjName;

    @Schema(description = "客户名称")
    private String clientName;

}



