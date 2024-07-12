package com.kge.energy.crm.dashboard.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DashBoardReq {
    @NotBlank
    @JsonProperty("StartTime")
    private String startTime;

    @NotBlank
    @JsonProperty("EndTime")
    private String endTime;
}
