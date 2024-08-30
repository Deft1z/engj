package com.kge.energy.crm.user.resp;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WxUserListResp {

    private Integer userId;

    private String realname;

    private String mobile;

    private String company;

    private String address;

    private String remark;

    private Integer tenantId;

}
