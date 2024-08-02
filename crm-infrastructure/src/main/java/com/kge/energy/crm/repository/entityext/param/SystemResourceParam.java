package com.kge.energy.crm.repository.entityext.param;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class SystemResourceParam {

    /**
     * 系统类型：applet、mgr
     */
    private String systemType;

    /**
     * 租户id
     */
    private Integer tenantId;

}
