package com.kge.energy.crm.dashboard.resp;

import lombok.Data;

@Data
public class StatisticResp1 {
    // 日新用户统计
    private Integer newUserCount;

    // 日新增
    private Integer newConsultingCount;

    private Integer newComplainCount;

    private Integer newContractCount;
}
