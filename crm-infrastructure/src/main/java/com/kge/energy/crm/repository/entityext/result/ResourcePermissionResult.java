package com.kge.energy.crm.repository.entityext.result;

import lombok.Data;

/**
 * @author wangjihua
 */
@Data
public class ResourcePermissionResult {

    private Integer resourceId;

    private Integer parentResourceId;

    private Integer referResourceId;

    private Integer level;

    private String type;

    private String name;

    private String displayName;

    private Integer sort;

    private Integer flag;

    private String remark;

    private Boolean authRead;

    private Boolean authWrite;

    private Boolean authDelete;

    private Boolean authAudit;


}