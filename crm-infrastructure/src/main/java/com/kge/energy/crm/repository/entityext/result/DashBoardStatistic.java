package com.kge.energy.crm.repository.entityext.result;

import lombok.Data;

@Data
public class DashBoardStatistic {

    // 日新用户统计
    private int newUserCount;

    // 日新增
    private int newConsultingCount;

    private int newComplainCount;

    private int newContractCount;

    private int sentNum;

    private int terminateNum;

    private int signNum;

    private int processingNum;
}
