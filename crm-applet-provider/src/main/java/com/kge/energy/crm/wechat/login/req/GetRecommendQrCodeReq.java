package com.kge.energy.crm.wechat.login.req;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class GetRecommendQrCodeReq {

    private Integer width = 400 ;

}
