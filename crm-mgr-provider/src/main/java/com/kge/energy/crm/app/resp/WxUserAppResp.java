package com.kge.energy.crm.app.resp;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangrongjun
 */
@NoArgsConstructor
@Data
public class WxUserAppResp {
    /**
     * 应用名称
     */
    private String name;

    /**
     * appId
     */
    private Integer appId;

    /**
     * openidId
     */
    private Integer openidId;

    /**
     * 绑定类型
     */
    private Integer bindType;

    /**
     * 绑定状态
     */
    private Integer bindingState;

    /**
     * 绑定时间
     */
    private String bindingTime;

    /**
     * 关联用户
     */
    private String externalAccount;
}
