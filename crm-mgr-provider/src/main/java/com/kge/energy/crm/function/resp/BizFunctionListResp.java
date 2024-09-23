package com.kge.energy.crm.function.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(name = "获取配置响应", description = "获取配置响应对象")
public class BizFunctionListResp {

    @Schema(description = "ID")
    private Integer id;

    @Schema(description = "业务模块名称")
    private String moduleName;

    @Schema(description = "业务模块编码")
    private String moduleCode;

    @Schema(description = "业务功能名称")
    private String functionName;

    @Schema(description = "业务功能编码")
    private String functionCode;

    @Schema(description = "租户ID")
    private Integer tenantId;

}
