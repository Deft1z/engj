package com.kge.energy.crm.workflow.resp;

import com.kge.energy.crm.repository.entityext.result.CmsCommentResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@Schema(description = "工单查询参数")
public class WfFormFlowResp {

    @Schema(description = "流转id")
    private Integer formFlowId;

    @Schema(description = "工单id")
    private Integer formId;

    @Schema(description = "操作时间")
    private String timeAction;

    @Schema(description = "用户id")
    private Integer userId;

    @Schema(description = "操作类型")
    private String actionType;

    @Schema(description = "操作内容")
    private String actionContent;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "子状态")
    private String subStatus;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建用户id")
    private Integer createUserId;

    @Schema(description = "更新用户id")
    private Integer modifyUserId;

    @Schema(description = "评论列表")
    private List<CmsCommentResult> commentList;
    
}