package com.kge.energy.crm.user.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kge.energy.crm.order.req.WorkOrderListReq;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class WxUserListReq {
    private Long CurrentPage;
    private Long PageSize;
    private Map<String,String> Sort;
    private WxUserListReq.SearchMapBean searchMap;

    @Data
    @Accessors(chain = true)
    public static class SearchMapBean {
        private String name;
    }
}
