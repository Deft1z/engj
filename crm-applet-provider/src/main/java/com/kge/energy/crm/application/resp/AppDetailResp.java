package com.kge.energy.crm.application.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(name = "APP详情响应参数", description = "APP详情响应参数")
public class AppDetailResp {

    @Schema(description = "应用ID")
    private Integer appId;

    @Schema(description = "应用名称")
    private String name;

    @Schema(description = "应用地址")
    private String appAddress;

    @Schema(description = "绑定地址")
    private String bindAddress;

}
