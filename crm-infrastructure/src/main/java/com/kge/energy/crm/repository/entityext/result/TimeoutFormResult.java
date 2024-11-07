package com.kge.energy.crm.repository.entityext.result;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class TimeoutFormResult {

    @NotBlank(message = "工单Id不能为空")
    private Integer formId;

    /**
     * 工单名称
     */
    @NotBlank(message = "工单名称不能为空")
    private String orderName;

    /**
     * 工单编号
     */
    @NotBlank(message = "工单编号不能为空")
    private String orderCode;

    /**
     * 流转时间
     */
    @NotBlank(message = "流转时间不能为空")
    private LocalDateTime flowTime;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 流转备注
     */
    private String actionContent;

    /**
     * 最新通知时间
     */
    private LocalDateTime lastNotifyTime;

    /**
     * 当前节点组织id
     */
    private Integer currentOrgId;

    /**
     * 租户id
     */
    private Integer tenantId;

}
