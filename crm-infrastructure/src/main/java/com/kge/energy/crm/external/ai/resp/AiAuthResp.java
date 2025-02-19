package com.kge.energy.crm.external.ai.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author zhengwenke
 * @date 2025/2/19 11:22
 */
@Data
@Schema(description = "AI服务注册/登录认证响应")
public class AiAuthResp {

    @Schema(description = "用户ID")
    private String id;

    @Schema(description = "用户邮箱")
    private String email;

    @Schema(description = "用户名")
    private String name;

    @Schema(description = "用户角色")
    private String role;

    @Schema(description = "用户token")
    private String token;

    @Schema(description = "token类型")
    @JsonProperty(value = "token_type")
    private String tokenType;

    @Schema(description = "token过期时间")
    @JsonProperty(value = "expires_at")
    private Long expiresAt;

}
