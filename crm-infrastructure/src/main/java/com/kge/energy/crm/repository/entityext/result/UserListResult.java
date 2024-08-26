package com.kge.energy.crm.repository.entityext.result;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class UserListResult {

    private Integer userId;

    private String name;

    private String realname;

    private String mobile;

    private Integer status;

    private String organizationName;
}
