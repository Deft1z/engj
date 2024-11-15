package com.kge.energy.crm.msg.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(name = "新增消息模板配置", description = "新增消息模板配置对象")
public class AddSysMsgTemplateReq {

    @Schema(description = "业务功能配置ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer bizFunctionId;

    @Schema(description = "消息模板编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String templateCode;

    @Schema(description = "消息模板内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String templateContent;

    @Schema(description = "是否启用", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer isEnabled;

    @Schema(description = "用途备注")
    private String remark;

    @Schema(description = "消息目标", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String msgTarget;

    @Schema(description = "elink消息请求参数")
    private String elinkParams;

    @Schema(description = "elink或短信消息请求参数")
    private String eSmsParams;

    @Schema(description = "玄武短信息消息请求参数")
    private String smsParams;

    @Schema(description = "微信小程序消息请求参数")
    private String appletParams;

    @Schema(description = "公众号消息请求参数，预留字段")
    private String offiaccountParams;

    @Schema(description = "邮件消息请求参数，预留字段")
    private String emailParams;

}
