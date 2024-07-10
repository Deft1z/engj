package com.kge.energy.crm.user.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class RoleUserResp {

    private Integer userId;

    private String realname;

    private String openId;
}
