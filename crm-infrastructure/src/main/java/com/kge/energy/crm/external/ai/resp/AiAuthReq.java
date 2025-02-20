package com.kge.energy.crm.external.ai.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @author zhengwenke
 * @date 2025/2/19 11:22
 */
@Data
@Builder
@Schema(description = "AI服务注册/登录认证响应")
public class AiAuthReq {

    @Schema(description = "用户名")
    private String name;

    @Schema(description = "用户邮箱")
    private String email;

    @Schema(description = "用户密码")
    private String password;

}
