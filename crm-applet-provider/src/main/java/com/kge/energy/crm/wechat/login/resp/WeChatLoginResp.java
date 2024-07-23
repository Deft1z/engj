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
public class WeChatLoginResp {

    @JsonProperty("session_key")
    private String sessionKey;

    @JsonProperty("unionid")
    private String unionId;

    @JsonProperty("openid")
    private String openId;

    @JsonProperty("errmsg")
    private String errMsg;

    @JsonProperty("errcode")
    private Integer errCode;

    private String token;

}
