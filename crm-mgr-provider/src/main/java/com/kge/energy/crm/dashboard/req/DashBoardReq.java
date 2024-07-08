package com.kge.energy.crm.dashboard.req;

import lombok.Data;

@Data
public class DashBoardReq {
    private String startTime;

    private String endTime;

    private int companyId;

    private String area;

    private int mode;
}
