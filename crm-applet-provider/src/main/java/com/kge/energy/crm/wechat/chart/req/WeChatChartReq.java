package com.kge.energy.crm.wechat.chart.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WeChatChartReq {
    @NotBlank
    private String startTime;

    @NotBlank
    private String endTime;
}
