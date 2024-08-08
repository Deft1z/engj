package com.kge.energy.crm.user.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class UserLoginResp {

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("tenant_id")
    private Integer tenantId;

    @JsonProperty("auth_token")
    private String authToken;

    private String msg;
}
