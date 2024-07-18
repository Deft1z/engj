package com.kge.energy.crm.order.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kge.energy.crm.common.page.PageReq;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author wangrongjun
 */
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class WxUserWorkOrderReq extends PageReq {

    @NotNull
    private Integer userId;

    private WorkOrderListReq.SearchMapBean searchMap;

    @Data
    @Accessors(chain = true)
    public static class SearchMapBean {

        private String name;

        private String status;

        private String businessName;

    }
}
