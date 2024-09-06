package com.kge.energy.crm.repository.entityext.param;

import com.kge.energy.crm.common.page.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class UserListParam extends PageReq {

    private Integer tenantId;

    private Integer organizationId;

    private String name;

    private String realname;

    private String mobile;

    private Integer status;

    private Integer roleId;
}
