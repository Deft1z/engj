package com.kge.energy.crm.repository.entityext.param;

import com.kge.energy.crm.common.page.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class SysOperateLogListParam extends PageReq {

    private Integer tenantId;

    private Integer operateModule;

    private String operateName;
}
