package com.kge.energy.crm.experience.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class EpControlEnabledResp {

    private Boolean submitWokrOrder = false;
}
