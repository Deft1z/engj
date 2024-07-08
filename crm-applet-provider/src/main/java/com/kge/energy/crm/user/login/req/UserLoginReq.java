package com.kge.energy.crm.user.login.req;

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
public class UserLoginReq {

    /**
     * 加密后的密码
     */
    @JsonProperty("passwd")
    private String pass;


    /**
     * 用户名
     */
    @JsonProperty("name")
    private String name;

}
