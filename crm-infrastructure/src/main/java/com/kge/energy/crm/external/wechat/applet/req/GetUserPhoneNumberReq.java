package com.kge.energy.crm.external.wechat.applet.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class GetUserPhoneNumberReq {

    private String code;

    @JsonProperty("openid")
    private String openId;

}
