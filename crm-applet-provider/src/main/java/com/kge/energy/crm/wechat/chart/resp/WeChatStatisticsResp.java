package com.kge.energy.crm.wechat.chart.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeChatStatisticsResp {
    private Integer newUserCount;
    private Integer newConsultingCount;
    private Integer newComplainCount;
    private Integer newContractCount;
}
