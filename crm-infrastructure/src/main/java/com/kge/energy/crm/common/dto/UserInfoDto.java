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
     * 租户ID
     */
    private Integer tenantId;

    /**
     * 系统类型：applet、mgr
     */
    private String systemType;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 微信小程序openid
     */
    private String wxOpenId;

    /**
     * 角色列表
     */
    private List<Role> roleList;

    /**
     * 组织列表
     */
    private List<Organization> organizationList;

    @Data
    @Accessors(chain = true)
    public static class Role {

        private Integer id;

        private String name;

        private String code;
    }

    @Data
    @Accessors(chain = true)
    public static class Organization {

        private Integer id;

        private String name;

    }
}
