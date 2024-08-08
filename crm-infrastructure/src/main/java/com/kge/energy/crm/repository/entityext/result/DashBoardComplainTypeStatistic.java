package com.kge.energy.crm.repository.entityext.result;

import lombok.Data;

@Data
public class DashBoardComplainTypeStatistic {
    private int slowNum;

    private int badQualityNum;

    private int badAttitude;

    private int total;
}
