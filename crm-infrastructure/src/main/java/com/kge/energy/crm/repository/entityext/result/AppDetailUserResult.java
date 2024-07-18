package com.kge.energy.crm.repository.entityext.result;


import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangrongjun
 */
@NoArgsConstructor
@Data
public class AppDetailUserResult {
    /**
     * 应用名称
     */
    private String name;

    /**
     * appid
     */
    private Integer appId;

    /**
     * openId
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
