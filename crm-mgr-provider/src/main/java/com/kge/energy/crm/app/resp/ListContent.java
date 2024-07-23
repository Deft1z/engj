package com.kge.energy.crm.app.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class ListContent {
    /**
     * 用户id
     */
    @JsonProperty("Uid")
    private Integer Uid;

    /**
     * 名称
     */
    @JsonProperty("Name")
    private String Name;

    /**
     * 电话
     */
    @JsonProperty("Mobile")
    private String Mobile;

    /**
     * 绑定类型
     */
    @JsonProperty("Apps")
    private List<App> Apps;
}
