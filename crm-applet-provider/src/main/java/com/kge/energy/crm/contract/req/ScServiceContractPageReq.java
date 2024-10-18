package com.kge.energy.crm.contract.req;

import com.kge.energy.crm.common.page.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "服务合同查询参数")
public class ScServiceContractPageReq extends PageReq {

    @Schema(description = "查询参数map")
    private ContracSearchMap searchMap;

    @Data
    @Accessors(chain = true)
    public static class ContracSearchMap {
        @Schema(description = "状态")
        private String status;
    }
    
}