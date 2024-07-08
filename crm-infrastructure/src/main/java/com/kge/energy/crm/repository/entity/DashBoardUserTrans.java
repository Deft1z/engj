package com.kge.energy.crm.repository.entity;

import lombok.Data;

@Data
public class DashBoardUserTrans {
    private int activeUserNum;

    private int orderNum;

    private int contractNum;

    private String activeUserTransRate;

    private String orderTransRate;
}
