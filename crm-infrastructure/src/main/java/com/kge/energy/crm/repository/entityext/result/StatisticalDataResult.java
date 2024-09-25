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


    @Data
    public static class User {

        @Schema(description = "累计客户数")
        private Integer totalCount;

        @Schema(description = "本月新增客户数")
        private Integer currentMonthCount;
    }

    @Data
    public static class Consulting {

        @Schema(description = "咨询单总数")
        private Integer totalCount;

        @Schema(description = "本月咨询单数")
        private Integer currentMonthCount;
    }


    @Data
    public static class Contract {

        @Schema(description = "已签订合同总数")
        private Integer totalCount;

        @Schema(description = "合同金额")
        private BigDecimal totalAmount;

        @Schema(description = "本月新签合同数")
        private Integer currentMonthCount;

        @Schema(description = "本月新增合同金额")
        private BigDecimal currentMonthAmount;
    }

}
