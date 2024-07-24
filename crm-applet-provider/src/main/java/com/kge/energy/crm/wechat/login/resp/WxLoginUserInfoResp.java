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

    private Integer userId;

    private String userName;

    private Integer roleId;

    private String roleName;

    private String type;

    private String mobile;

    private String realname;

    private String company;

    private String address;

    private List<Organization> organizationList;

    @NoArgsConstructor
    @Data
    public static class Organization {

        private Integer id;

        private String name;

        private String authCode;
    }
}
