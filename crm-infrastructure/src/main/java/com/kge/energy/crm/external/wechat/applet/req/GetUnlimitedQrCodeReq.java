package com.kge.energy.crm.external.wechat.applet.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class GetUnlimitedQrCodeReq {

    private String page;

    @NotBlank
    private String scene;

    private Integer width;

    private String env_version;

}
