package com.kge.energy.crm.repository.entityext.param;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DashBoardParam {

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private List<String> dateList;

    private Integer orgId;

    private String area;
}
