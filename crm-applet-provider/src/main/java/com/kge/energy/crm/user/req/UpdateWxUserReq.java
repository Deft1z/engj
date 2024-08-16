package com.kge.energy.crm.user.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangjihua
 */
@NoArgsConstructor
@Data
public class UpdateWxUserReq {

    @NotNull
    private Integer userId;

    private String mobile;

    private String realname;

    private String company;

    private String address;

    private String remark;
}
