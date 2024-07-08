package com.kge.energy.crm.wechat.login.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 小程序首页登录返回值
 *
 * @author zqy
 */
@Data
@Accessors(chain = true)
public class WechatLoginResp {

    @JsonProperty("errcode")
    private Integer code;

    @JsonProperty("token")
    private String token;

    @JsonProperty("errmsg")
    private String msg;

    @JsonProperty("openid")
    private String openId ;

    @JsonProperty("session_key")
    private String key;

    @JsonProperty("unionid")
    private String unionId;


}
