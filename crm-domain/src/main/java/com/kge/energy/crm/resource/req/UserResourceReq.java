package com.kge.energy.crm.resource.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class UserResourceReq {

    /**
     * 系统类型：applet、mgr
     */
    @NotBlank
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
