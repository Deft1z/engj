package com.kge.energy.crm.app.req;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Data
@Accessors(chain = true)
public class ProjectDelReq {
    /**
     * 第几页
     */
    private Integer openid;

    /**
     * 限制一页的数量
     */
    private Integer projectid;
}
