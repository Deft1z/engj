package com.kge.energy.crm.repository.entityext.param;

import com.kge.energy.crm.common.page.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class BizFunctionListParam extends PageReq {

    private Integer tenantId;

    private String moduleName;

    private String moduleCode;

    private String functionName;

    private String functionCode;


}
