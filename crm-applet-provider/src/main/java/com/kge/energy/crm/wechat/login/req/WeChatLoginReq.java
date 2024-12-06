package com.kge.energy.crm.wechat.login.req;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户登录请求体
 *
 * @author zqy
 */
@Data
@Accessors(chain = true)
public class WeChatLoginReq {

    /**
     * token值
     */
    private String token;

    /**
     * 手机
     */
//    private String mobile;

    /**
     * 用户名
     */
    private String jsCode;

    /**
     * 用户名
     */
    private Integer recommendUserId;

}
