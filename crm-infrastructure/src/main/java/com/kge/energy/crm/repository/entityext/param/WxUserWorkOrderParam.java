package com.kge.energy.crm.repository.entityext.param;

import com.kge.energy.crm.common.page.PageReq;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WxUserWorkOrderParam extends PageReq {

    private Integer userId;

    private Integer roleId;

    private WorkOrderListParam.SearchMapBean searchMap;

    @Data
    @Accessors(chain = true)
    public static class SearchMapBean {

        private String name;

        private String status;

        private String businessName;

        private String starttime;

        private String endtime;

    }
}
