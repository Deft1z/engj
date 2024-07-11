package com.kge.energy.crm.user.resp;

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
public class CurrentUserInfoResp {

    private Integer userId;

    private String userName;

    private Integer roleId;

    private String roleName;

    private List<OrganizationListBean> organizationList;

    @NoArgsConstructor
    @Data
    public static class OrganizationListBean {

        private Integer id;

        private String name;
        
        private String authCode;
    }
}
