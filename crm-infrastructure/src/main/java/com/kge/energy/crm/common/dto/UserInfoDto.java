package com.kge.energy.crm.common.dto;

import com.kge.platform.framework.common.dto.CommonUserInfoDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户信息上下文
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class UserInfoDto extends CommonUserInfoDto {

    /**
     * 用户类型
     */
    private String type;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 微信小程序openid
     */
    private String openId;
}
