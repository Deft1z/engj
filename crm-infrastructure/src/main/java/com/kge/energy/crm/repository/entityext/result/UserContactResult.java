package com.kge.energy.crm.repository.entityext.result;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhengwenke
 */
@Data
@Accessors(chain = true)
public class UserContactResult {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 微信openId
     */
    private String openId;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 组织id
     */
    private Integer organizationId;

}
