package com.kge.energy.crm.external.wechat.applet.req.complain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReplyComplainData {

    @JsonProperty("Value")
    private String data;

}
