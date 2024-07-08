package com.kge.energy.crm.user.login.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户登录返回值
 *
 * @author zqy
 */
@Data
@Accessors(chain = true)
public class UserLoginResp {

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("auth_token")
    private String token;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("tenant_id")
    private Integer tenantId ;

}
