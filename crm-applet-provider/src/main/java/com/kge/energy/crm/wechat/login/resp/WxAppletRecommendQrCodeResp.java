package com.kge.energy.crm.wechat.login.resp;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;


@NoArgsConstructor
@Data
@Accessors(chain = true)
public class WxAppletRecommendQrCodeResp {

    private String url;

    private String expireTime;
}
