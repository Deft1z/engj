package com.kge.energy.crm.external.wechat.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class WeChatAppletStableAccessTokenReq {

    @JsonProperty("grant_type")
    private String grantType = "client_credential";

    private String appid;

    private String secret;

    @JsonProperty("force_refresh")
    private Boolean forceRefresh = false;


}
