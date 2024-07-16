package com.kge.energy.crm.user.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class WxUserListReq {
    private Long CurrentPage;
    private Long PageSize;
    private Map<String,String> Sort;
    @JsonProperty("name")
    private String NameF;
}
