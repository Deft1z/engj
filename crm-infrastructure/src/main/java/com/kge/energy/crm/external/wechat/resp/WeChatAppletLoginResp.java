package com.kge.energy.crm.external.wechat.resp;

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
public class WeChatAppletLoginResp {


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


}
