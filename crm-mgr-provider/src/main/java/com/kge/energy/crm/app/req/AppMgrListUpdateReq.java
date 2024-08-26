package com.kge.energy.crm.app.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AppMgrListUpdateReq {

    @JsonProperty(value = "app_id")
    @NotNull
    private Integer appId;

    @NotNull
    private String name;

    @JsonProperty(value = "bind_address")
    private String bindAddress;

    private String remark;

    @Schema(description = "是否常用")
    private Boolean commonlyUsed;
}
