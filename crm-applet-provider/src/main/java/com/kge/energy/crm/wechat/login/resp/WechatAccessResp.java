package com.kge.energy.crm.wechat.login.resp;

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
public class WechatAccessResp {


    @JsonProperty("session_key")
    private String sessionKey;


    @JsonProperty("unionid")
    private String unionId;

    @JsonProperty("openid")
    private String openId;


    @JsonProperty("token")
    private String token;


    @JsonProperty("errmsg")
    private String msg;


    @JsonProperty("errcode")
    private Integer code;



}
