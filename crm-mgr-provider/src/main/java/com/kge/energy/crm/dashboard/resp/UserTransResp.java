package com.kge.energy.crm.dashboard.resp;

import lombok.Data;

@Data
public class UserTransResp {
    private Integer newUserNum;

    private Integer orderNum;

    private Integer contractNum;

    private String newUserTransRate;

    private String orderTransRate;
}
