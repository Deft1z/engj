package com.kge.energy.crm.repository.entityext.result;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangrongjun
 */
@NoArgsConstructor
@Data
public class WxUserAppResult {
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
    private String bindType;

    /**
     * 绑定状态
     */
    private String bindingState;

    /**
     * 绑定时间
     */
    private String bindingTime;

    /**
     * 关联用户
     */
    private String externalAccount;
}
