package com.kge.energy.crm.dashboard.resp;

import lombok.Data;

import java.util.List;

@Data
public class EvaluateListResp {
    private List<EvaluateResp> list;
    private double average;
}
