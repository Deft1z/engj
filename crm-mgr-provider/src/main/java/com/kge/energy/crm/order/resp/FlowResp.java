package com.kge.energy.crm.order.resp;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@NoArgsConstructor
@Data
@Accessors
public class FlowResp {

    private Integer formFlowId;

    private Integer formId;

    private String timeAction;

    private Integer userId;

    private String actionType;

    private String actionContent;

    private String status;

    private String subStatus;

    private String remark;

    private Integer createUserId;

    private Integer modifyUserId;
}
