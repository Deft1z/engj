package com.kge.energy.crm.repository.entityext.param;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 获取运维报告列表
 *
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class OmReportListParam {

    /**
     * 操作员
     */
    private String operator;

}
