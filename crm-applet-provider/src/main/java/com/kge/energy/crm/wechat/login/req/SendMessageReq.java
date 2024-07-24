package com.kge.energy.crm.wechat.login.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class SendMessageReq {

    @NotBlank
    private String templateId;

    @NotBlank
    private String toUserOpenId;

    @NotBlank
    private String page;

    @NotNull
    private String data;

    @NotBlank
    private String miniprogramState;

    private String lang;
}
