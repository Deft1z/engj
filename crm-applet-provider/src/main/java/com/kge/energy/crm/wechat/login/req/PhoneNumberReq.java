package com.kge.energy.crm.wechat.login.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class PhoneNumberReq {

    @NotBlank
    private String code;

    @NotBlank
    private String openid;
}
