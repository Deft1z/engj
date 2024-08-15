package com.kge.energy.crm.repository.entityext.param;

import com.kge.energy.crm.common.page.PageReq;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class RoleListParam extends PageReq {

    private Integer tenantId;

    private String systemType;

    private List<String> excludeCodes;

}
