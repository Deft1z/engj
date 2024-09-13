package com.kge.energy.crm.comment.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(name = "新增工单评论", description = "新增工单评论传参")
public class WfFormCommentReq extends CmsCommentAddReq {

    @NotNull
    @Schema(description = "评论内容，必传")
    private Integer formId;

}
