package com.kge.energy.crm.log.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@Schema(name = "登录日志列表响应参数", description = "登录日志列表响应参数")
public class SysLoginLogListResp {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "链路ID")
    private String traceId;

    @Schema(description = "租户名")
    private String tenantName;

    @Schema(description = "登录人ID")
    private Integer userId;

    @Schema(description = "登录人账号")
    private String userName;

    @Schema(description = "登录人手机号")
    private String userMobile;

    @Schema(description = "登录人名称")
    private String userRealname;

    @Schema(description = "登录时间")
    private String loginTime;

    @Schema(description = "登录平台")
    private String loginPlatform;

    @Schema(description = "登录结果")
    private String loginResult;

    @Schema(description = "详情")
    private String loginMessage;

}
