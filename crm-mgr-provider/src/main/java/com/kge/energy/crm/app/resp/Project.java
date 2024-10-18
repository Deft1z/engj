package com.kge.energy.crm.app.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * wangrongjun
 */
@NoArgsConstructor
@Data
public class Project {
    /**
     * 项目名称
     */
    @JsonProperty("Name")
    private String name;

    /**
     * 项目id
     */
    @JsonProperty("Id")
    private Integer id;
}
