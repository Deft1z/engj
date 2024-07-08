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
public class WechatLoginReq {

    /**
     * 手机
     */
    @JsonProperty("mobile")
    private String mobile;

    /**
     * 用户名
     */
    @JsonProperty("jsCode")
    private String jsCode;

}
