package com.kge.energy.crm.app.resp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wangrongjun
 */
@NoArgsConstructor
@Data
public class UserResp {
    /**
     * 用户id
     */
    private Integer userid;

    /**
     * 应用名称
     */
    private String name;

    /**
     * appids
     */
    private List<Integer> appids;
}
