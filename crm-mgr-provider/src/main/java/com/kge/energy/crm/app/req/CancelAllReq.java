package com.kge.energy.crm.app.req;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@NoArgsConstructor
@Data
@Accessors(chain = true)
public class CancelAllReq {
    /**
     * openid
     */
    private List<Integer> openid;
}
