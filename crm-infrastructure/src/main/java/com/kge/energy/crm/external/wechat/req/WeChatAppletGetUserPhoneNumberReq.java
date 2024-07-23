package com.kge.energy.crm.external.wechat.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class WeChatAppletGetUserPhoneNumberReq {

    private String code;

    @JsonProperty("openid")
    private String openId;

}
