package com.kge.energy.crm.repository.entityext.result;

import lombok.Data;

@Data
public class AppMgrListResult {

    private Integer appId;
    private String name;
    private Integer bindType;
    private String appAddress;
    private String interfaceAddress;
    private String bindAddress;
    private Integer flag;
    private String remark;

}
