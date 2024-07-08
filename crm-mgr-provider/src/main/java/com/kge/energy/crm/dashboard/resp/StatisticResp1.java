package com.kge.energy.crm.dashboard.resp;

import lombok.Data;

@Data
public class StatisticResp1 {
    // 日新用户统计
    private int newUserCount;

    // 日新增
    private int newConsultingCount;

    private int newComplainCount;

    private int newContractCount;
}
