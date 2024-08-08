package com.kge.energy.crm.resource.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class MenuNodeResp {
    private Integer id;

    private Integer ParentId;

    private Integer referId;

    private Integer level;

    private String path;

    private String originParentPath;

    private String name;

    private Integer sort;

    private Integer flag;

    private List<MenuNodeResp> children;

    private Map<String,Boolean> permission;
}
