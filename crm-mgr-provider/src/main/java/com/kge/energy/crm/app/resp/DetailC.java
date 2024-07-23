package com.kge.energy.crm.app.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wangrongjun
 */
@NoArgsConstructor
@Data
public class DetailC {

    /**
     * 用户Id
     */
    @JsonProperty("Uid")
    private Integer Uid;

    /**
     * 应用名称
     */
    @JsonProperty("Name")
    private String name;

    /**
     * 电话
     */
    @JsonProperty("Mobile")
    private String Mobile;

    /**
     * 组织id
     */
    @JsonProperty("Apps")
    private List<App> Apps;
}
