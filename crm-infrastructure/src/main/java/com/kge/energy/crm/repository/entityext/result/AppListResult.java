package com.kge.energy.crm.repository.entityext.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class AppListResult {

    public AppListResult(Integer appId, String name, String bindingTime) {
        this.appId = appId;
        this.name = name;
        this.bindingTime = bindingTime;
    }

    private Integer appId;
    private String name;
    private String bindingTime;
    private String filepath;

}
