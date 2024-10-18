package com.kge.energy.crm.log.req;

import com.kge.energy.crm.common.page.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(name = "登录日志列表请求参数", description = "登录日志列表请求参数")
public class SysLoginLogListReq extends PageReq {

    @Schema(description = "租户ID")
    private Integer tenantId;

    @Schema(description = "登录平台，1-微信小程序, 2-pc")
    private Integer loginPlatform;

    @Schema(description = "登录结果，0-失败, 1-成功")
    private Integer loginResult;
}
