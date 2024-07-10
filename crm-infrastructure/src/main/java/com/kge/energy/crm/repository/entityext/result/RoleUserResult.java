package com.kge.energy.crm.repository.entityext.result;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class RoleUserResult {

    private Integer userId;

    private String realname;

    private String openId;
}
