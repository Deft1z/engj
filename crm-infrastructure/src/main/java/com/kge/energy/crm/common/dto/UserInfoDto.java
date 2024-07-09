package com.kge.energy.crm.common.dto;

import com.kge.platform.framework.common.dto.CommonUserInfoDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 用户信息上下文
 *
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
    private String wxOpenId;

    /**
     * 角色ID
     */
    private Integer roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 组织列表
     */
    private List<Organization> organizationList;

    @Data
    @Accessors(chain = true)
    public static class Organization {

        private Integer id;

        private String name;

        private String authCode;
    }
}
