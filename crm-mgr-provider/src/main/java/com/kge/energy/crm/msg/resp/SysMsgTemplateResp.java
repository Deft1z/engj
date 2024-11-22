package com.kge.energy.crm.msg.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 消息模板配置列表(SysMsgTemplateList)响应对象
 */
@Data
@Accessors(chain = true)
@Schema(name = "获取消息模板配置列表响应", description = "获取消息模板配置列表响应对象")
public class SysMsgTemplateResp {

    @Schema(description = "消息模板ID")
    private Integer id;

    @Schema(description = "业务功能ID")
    private Integer bizFunctionId;

    @Schema(description = "消息模板编码")
    private String templateCode;

    @Schema(description = "消息目标 user 用户个人消息 role 角色多人消息")
    private String msgTarget;

    @Schema(description = "用途备注")
    private String remark;

    @Schema(description = "是否启用 0-禁用，1-启用")
    private Integer isEnabled;

    @Schema(description = "消息模板内容")
    private String templateContent;

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
