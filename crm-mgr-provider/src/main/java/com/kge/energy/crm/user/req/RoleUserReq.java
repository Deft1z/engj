package com.kge.energy.crm.user.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class RoleUserReq {

    @NotNull
    private Integer roleId;
}
