package com.kge.energy.crm.app.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class App {

    /**
     * 应用名称
     */
    @JsonProperty("Name")
    private String name;

    /**
     * 应用Id
     */
    @JsonProperty("Id")
    private Integer id;

    /**
     * 应用Id
     */
    @JsonProperty("Projects")
    private List<Project> projects;

    /**
     * 组织id
     */
    @JsonProperty("Oid")
    private Integer oid;

}
