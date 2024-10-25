package com.kge.energy.crm.wechat.login.resp;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;



@NoArgsConstructor
@Data
@Accessors(chain = true)
public class WxAppletRecommendQrCodeResp {


    private byte[] bf;

}
