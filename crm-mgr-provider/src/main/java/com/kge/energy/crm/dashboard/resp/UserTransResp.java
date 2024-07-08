package com.kge.energy.crm.dashboard.resp;

import lombok.Data;

@Data
public class UserTransResp {
    private int newUserNum;

    private int orderNum;

    private int contractNum;

    private String newUserTransRate;

    private String orderTransRate;
}
