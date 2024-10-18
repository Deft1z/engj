package com.kge.energy.crm.wechat.chart.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeChatChartResp {
    private Integer newUserNum;
    private Integer orderNum;
    private Integer contractNum;
    private String newUserTransRate;
    private String orderTransRate;
}
