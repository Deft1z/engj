package com.kge.energy.crm.app.req;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Data
@Accessors(chain = true)
public class BindReq {

    /**
     * openid
     */
    private Integer openid;

    /**
     * userid
     */
    private Integer userid;
}
