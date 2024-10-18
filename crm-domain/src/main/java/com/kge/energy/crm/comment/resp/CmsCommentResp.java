package com.kge.energy.crm.comment.resp;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CmsCommentResp {
    private Integer commentId;
    private String name;
    private String replyName;
    private String content;
    private String date;
}
