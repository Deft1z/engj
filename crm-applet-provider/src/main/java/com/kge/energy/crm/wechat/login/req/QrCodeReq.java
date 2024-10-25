package com.kge.energy.crm.wechat.login.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class QrCodeReq {

    private Double width;
}
