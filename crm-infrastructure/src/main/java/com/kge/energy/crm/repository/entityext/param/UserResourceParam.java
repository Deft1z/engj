package com.kge.energy.crm.repository.entityext.param;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class UserResourceParam {

    /**
     * 系统类型：applet、mgr
     */
    private String systemType;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 租户id
     */
    private Integer tenantId;

}
