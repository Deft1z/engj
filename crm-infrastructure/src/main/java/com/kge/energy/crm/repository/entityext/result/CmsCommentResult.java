package com.kge.energy.crm.repository.entityext.result;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CmsCommentResult {
    private Integer commentId;
    private String name;
    private String replyName;
    private String content;
    private String date;
}
