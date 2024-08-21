package com.kge.energy.crm.wechat.login.resp;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author wangjihua
 */
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class WxLoginUserInfoResp {

    /**
     * 租户ID
     */
    private Integer tenantId;

    /**
     * 租户名称
     */
    private String tenantName;

    private Integer userId;

    private String userName;

    private String type;

    private String mobile;

    private String realname;

    private String company;

    private String address;

    /**
     * 角色列表
     */
    private List<Role> roleList;

    private List<Organization> organizationList;

    @Data
    @Accessors(chain = true)
    public static class Role {

        private Integer id;

        private String name;

        private String code;
    }


    @NoArgsConstructor
    @Data
    public static class Organization {

        private Integer id;

        private String name;

    }
}
