package com.kge.energy.crm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CmsCommentBizType {
    NZGF(1, "南综光伏"),
    ORDER(2, "业务工单"),
    COMPLAIN(3, "投诉建议");

    private final Integer code;

    private final String desc;
}
