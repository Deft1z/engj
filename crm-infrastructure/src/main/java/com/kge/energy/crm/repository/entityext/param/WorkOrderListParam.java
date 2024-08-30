package com.kge.energy.crm.repository.entityext.param;

import com.kge.energy.crm.common.page.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class WorkOrderListParam extends PageReq {


    private Integer formTypeId;

    private Integer tenantId;

    private SearchMapBean searchMap;

    @Data
    @Accessors(chain = true)
    public static class SearchMapBean {

        private String name;

        private String status;

        private LocalDateTime starttime;

        private LocalDateTime endtime;

        private String onlyMe;

        private String businessName;

    }
}
