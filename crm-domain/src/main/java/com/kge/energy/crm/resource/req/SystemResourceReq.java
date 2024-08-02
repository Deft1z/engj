package com.kge.energy.crm.resource.req;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class SystemResourceReq {

    /**
     * 系统类型：applet、mgr
     */
    private String systemType;

    /**
     * 租户id
     */
    private Integer tenantId;

}
