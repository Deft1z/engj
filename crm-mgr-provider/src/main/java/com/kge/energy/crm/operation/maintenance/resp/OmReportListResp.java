package com.kge.energy.crm.operation.maintenance.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 获取运维报告列表请求体
 *
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class OmReportListResp {

    private Integer formId;

    private String operator;
}
