package com.kge.energy.crm.repository.entityext.result;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AppletCommentResult {
    private Integer id;
    private Integer pid;
    private Integer uid;
    private Integer thumb;
    private Integer status;
    private String content;
    private String date;
    private String name;
    private String rname;
    private Integer calLikeNum;
}
