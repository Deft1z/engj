package com.kge.energy.crm.app.req;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Data
@Accessors(chain = true)
public class AddProReq {
    /**
     * openId
     */
    private Integer openid;

    /**
     * Appid
     */
    private Integer appid;

    /**
     * 名称
     */
    private String name;

    /**
     * 项目id
     */
    private Integer projectid;
}
