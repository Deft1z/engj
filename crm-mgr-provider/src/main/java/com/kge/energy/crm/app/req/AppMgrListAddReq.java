package com.kge.energy.crm.app.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AppMgrListAddReq {

    @NotNull
    private String name;

    @JsonProperty(value = "bind_address")
    private String bindAddress;

    private String remark;
}
