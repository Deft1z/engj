package com.kge.energy.crm.app.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AppMgrListUpdateReq {

    @NotNull
    private Integer appId;

    @NotNull
    private String name;

    private Integer bindType;

    private String bindAddress;

    private String appAddress;

    private String interfaceAddress;

    @Schema(description = "是否常用")
    private Boolean commonlyUsed;
}
