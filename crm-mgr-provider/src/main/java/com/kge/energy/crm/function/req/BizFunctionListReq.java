package com.kge.energy.crm.function.req;

import com.kge.energy.crm.common.page.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "获取配置请求", description = "获取配置请求对象")
public class BizFunctionListReq extends PageReq {

    @Schema(description = "租户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer tenantId;

    @Schema(description = "业务模块名称")
    private String moduleName;

    @Schema(description = "业务模块编码")
    private String moduleCode;

    @Schema(description = "业务功能名称")
    private String functionName;

    @Schema(description = "业务功能编码")
    private String functionCode;


}
