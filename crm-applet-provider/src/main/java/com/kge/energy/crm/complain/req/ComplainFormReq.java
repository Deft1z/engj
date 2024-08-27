package com.kge.energy.crm.complain.req;

import com.kge.energy.crm.common.page.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "投诉工单查询参数")
public class ComplainFormReq extends PageReq {

    @Schema(description = "查询参数map")
    private SearchFormMap searchMap;

    @Data
    @Accessors(chain = true)
    public static class SearchFormMap {
        @Schema(description = "状态")
        private String status;
        @Schema(description = "名称")
        private String name;
        @Schema(description = "只查userId")
        private String onlyMe;
        @Schema(description = "业务名称")
        private String businessName;
    }
    
}