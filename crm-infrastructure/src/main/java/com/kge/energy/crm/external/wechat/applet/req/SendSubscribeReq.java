package com.kge.energy.crm.external.wechat.applet.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class SendSubscribeReq {

    /**
     * 所需下发的订阅模板id
     */
    @NotBlank
    @JsonProperty("template_id")
    private String templateId;

    /**
     * 点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转
     */
    private String page;

    /**
     * 接收者（用户）的 openid
     */
    @NotBlank
    @JsonProperty("touser")
    private String toUserOpenId;

    /**
     * 模板内容，格式形如 { "key1": { "value": any }, "key2": { "value": any } }的object
     */
    @NotBlank
    private String data;

    /**
     * 跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
     */
    @NotBlank
    @JsonProperty("miniprogram_state")
    private String miniprogramState;

    /**
     * 进入小程序查看”的语言类型，支持zh_CN(简体中文)、en_US(英文)、zh_HK(繁体中文)、zh_TW(繁体中文)，默认为zh_CN
     */
    @NotBlank
    private String lang = "zh_CN";
}
