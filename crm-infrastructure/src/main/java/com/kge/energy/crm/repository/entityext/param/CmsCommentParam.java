package com.kge.energy.crm.repository.entityext.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CmsCommentParam {
    /**
     * 业务数据id
     */
    private Integer bizDataId;

    /**
     * 业务类型:1-南综光伏项目 2-业务工单 3-投诉建议
     */
    private Integer bizType;
}
