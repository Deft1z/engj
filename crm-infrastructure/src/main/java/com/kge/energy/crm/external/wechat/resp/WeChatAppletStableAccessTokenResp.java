package com.kge.energy.crm.external.wechat.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author wangjihua
 */
@Data
public class WeChatAppletStableAccessTokenResp {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Integer expiresIn;
}
