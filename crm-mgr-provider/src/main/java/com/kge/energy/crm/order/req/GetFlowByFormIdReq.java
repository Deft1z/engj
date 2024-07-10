package com.kge.energy.crm.order.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class GetFlowByFormIdReq {

    @NotNull
    private Integer formId;
}
