package com.kge.energy.crm.repository.entityext.result;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangrongjun
 */
@Data
@Accessors(chain = true)
public class OpenIdModelList {

    private Integer id;

    private Integer appid;

    private Integer state;

    private Integer flag;

    private Integer uid;

    private Integer oid;

    private Integer pid;

    private Integer rflag;

    private Integer proaid;

    private String pname;

    private String realname;

    private String name;

    private String mobile;

    private String remark;
}
