package com.kge.energy.crm.external.wechat.applet.req;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("env_version")
    private String envVersion;

}
