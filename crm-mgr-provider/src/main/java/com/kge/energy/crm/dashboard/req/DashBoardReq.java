package com.kge.energy.crm.dashboard.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DashBoardReq {
    // TODO 这里要让前端将参数改为小驼峰形式，改好后去掉JsonProperty注解
    @NotBlank
    @JsonProperty("StartTime")
    private String startTime;

    @NotBlank
    @JsonProperty("EndTime")
    private String endTime;
}
