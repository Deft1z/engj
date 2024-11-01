package com.kge.energy.crm.repository.entityext.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 调查表单表(BSurvey)响应对象
 *
 * @author zhengwenke
 * @since 2024-10-30 09:27:34
 */
@Data
@Accessors(chain = true)
@Schema(description = "调查表单表响应对象")
public class BSurveyResult {

    @Schema(description = "主键id")
    private Integer id;

    @Schema(description = "表单编码")
    private String surveyCode;

    @Schema(description = "调查表单名称")
    private String surveyName;

    @Schema(description = "备注")
    private String remark;

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

    @Schema(description = "调查表单项")
    private List<BSurveyItemResult> surveyItems;

}



