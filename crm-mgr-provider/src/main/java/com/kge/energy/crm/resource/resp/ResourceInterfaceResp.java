package com.kge.energy.crm.resource.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(name = "资源接口响应参数", description = "资源接口响应参数")
public class ResourceInterfaceResp {

    @Schema(description = "接口ID")
    private Integer id;

    @Schema(description = "接口名称")
    private String interfaceName;

    @Schema(description = "接口地址")
    private String interfaceUrl;

    @Schema(description = "请求方式（GET POST PUT DELETE）")
    private String requestMethod;

    @Schema(description = "状态（0正常 1停用）")
    private Integer status;

}
