package com.kge.energy.crm.wechat.chart.resp;

import com.kge.energy.crm.repository.entityext.result.StartEva;
import lombok.Data;

import java.util.List;

@Data
public class WeChatEvaluateResp {
    private List<StartEvaResp> list;
    private Float average;
}
