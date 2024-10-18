package com.kge.energy.crm.resource.req;

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
@Schema(name = "资源接口列表请求参数", description = "资源接口列表请求参数")
public class ResourceInterfaceListReq extends PageReq {

    @NotNull
    @Schema(description = "资源ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer resourceId;

    @Schema(description = "接口名称")
    private String interfaceName;

    @Schema(description = "接口地址")
    private String interfaceUrl;

}
