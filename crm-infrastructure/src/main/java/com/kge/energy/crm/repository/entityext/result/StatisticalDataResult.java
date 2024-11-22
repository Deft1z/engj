package com.kge.energy.crm.repository.entityext.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
public class StatisticalDataResult {

    @Schema(description = "用户统计")
    private User user;

    @Schema(description = "咨询单统计")
    private Consulting consulting;

    @Schema(description = "合同统计")
    private Contract contract;

    @Schema(description = "投诉统计")
    private Complain complain;

    @Data
    public static class User {

        @Schema(description = "推广客户数")
        private Integer recommendCount;

        @Schema(description = "新增客户数")
        private Integer newCount;

        @Schema(description = "累计客户数")
        private Integer totalCount;
    }

    @Data
    public static class Consulting {

        @Schema(description = "新增咨询单数")
        private Integer newCount;

        @Schema(description = "咨询单总数")
        private Integer totalCount;
    }


    @Data
    public static class Contract {

        @Schema(description = "新签订合同数")
        private Integer newCount;

        @Schema(description = "已签订合同总数")
        private Integer totalCount;

        @Schema(description = "新增合同金额")
        private BigDecimal newAmount;

        @Schema(description = "合同总金额")
        private BigDecimal totalAmount;
    }

    @Data
    public static class Complain {

        @Schema(description = "新增工单投诉")
        private Integer newOrderCount;

        @Schema(description = "新增合同投诉")
        private Integer newContractCount;
    }

}
