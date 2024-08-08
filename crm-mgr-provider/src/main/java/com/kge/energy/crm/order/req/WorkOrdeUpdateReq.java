package com.kge.energy.crm.order.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangjihua
 */
@NoArgsConstructor
@Data
public class WorkOrdeUpdateReq {

    @NotNull
    private Integer formId;

    private String content;

    /**
     * 1 => 流转工单（工单分配）  2 => 回复工单（工单处理） 3 => 完成工单  4 => 终止工单
     */
    @NotNull
    private Integer type;

    /**
     * 1 => 集团 2 => 二级公司
     */
    @NotNull
    private Integer level;

    @NotNull
    private Integer currentOrgId;

    @NotNull
    private Integer currentRoleId;
}
