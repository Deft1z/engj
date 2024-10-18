package com.kge.energy.crm.repository.entityext.param;

import com.kge.energy.crm.common.page.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class DataPermissionListParam extends PageReq {

    private Integer bizFunctionId;

}
