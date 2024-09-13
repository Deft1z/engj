package com.kge.energy.crm.comment.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(name = "新增评论", description = "新增评论传参")
public class CmsCommentAddReq {

    @Schema(description = "父评论id,新增评论不用传，回复评论需要传回复的评论id")
    private Integer parentCommentId;

    @NotBlank
    @Schema(description = "评论内容，必传")
    private String content;

    /**
     * 业务数据id
     */
    //@Schema(description = "业务数据id，后端字段不用传")
    private Integer bizDataId;

    /**
     * 业务类型:1-南综光伏项目 2-业务工单 3-投诉建议
     */
    //@Schema(description = "业务类型:1-南综光伏项目 2-业务工单 3-投诉建议，后端字段不用传")
    private Integer bizType;

}
