package com.kge.energy.crm.wechat.login.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户登录请求体
 *
 * @author zqy
 */
@Data
@Accessors(chain = true)
public class WechatAccessReq {


    @JsonProperty("appId")
    private String appId;


    @JsonProperty("appSecret")
    private String appSecret;

    @JsonProperty("grant_type")
    private String grantType;


    @JsonProperty("js_code")
    private String code;

}
