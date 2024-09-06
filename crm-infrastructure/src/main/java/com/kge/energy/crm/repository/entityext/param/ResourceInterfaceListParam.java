package com.kge.energy.crm.repository.entityext.param;

import com.kge.energy.crm.common.page.PageReq;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class ResourceInterfaceListParam extends PageReq {

    private Integer resourceId;

    private String interfaceName;

    private String interfaceUrl;

}
