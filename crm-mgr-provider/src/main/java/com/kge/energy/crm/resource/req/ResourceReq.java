package com.kge.energy.crm.resource.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResourceReq {

    @JsonProperty("user_id")
    private Integer userId;
}
