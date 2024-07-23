package com.kge.energy.crm.app.req;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Data
@Accessors(chain = true)
public class  ListReq {

    /**
     * 第几页
     */
    private Integer page;

    /**
     * 限制一页的数量
     */
    private Integer limit;

    /**
     * appid
     */
    private Integer appid;

    /**
     * 电话号码
     */
    private String mobile;

    /**
     * 用户姓名
     */
    private String name;

}
