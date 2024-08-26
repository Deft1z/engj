package com.kge.energy.crm.external.wechat.applet.req.complain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReplyComplainReq {

    @JsonProperty("thing1")
    private ReplyComplainData content;

    @JsonProperty("thing2")
    private ReplyComplainData remark;

    @JsonProperty("time3")
    private ReplyComplainData time;
}
