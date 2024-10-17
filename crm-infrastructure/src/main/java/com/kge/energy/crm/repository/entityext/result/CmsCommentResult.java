package com.kge.energy.crm.repository.entityext.result;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class CmsCommentResult {
    private Integer commentId;
    private Integer parentCommentId;
    private String name;
    private String replyName;
    private String content;
    private String date;
    private List<ChildrenCommentResult> childrenCommentList;

    @Data
    @Accessors(chain = true)
    public static class ChildrenCommentResult {
        private Integer commentId;
        private Integer parentCommentId;
        private String name;
        private String replyName;
        private String content;
        private String date;
    }
}
