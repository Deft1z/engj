package com.kge.energy.crm.repository.entityext.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
@Schema(description = "运营看板统计响应对象")
public class DashboardStatResult {

    @Schema(description = "统计指标描述")
    private String statDesc;
    @Schema(description = "统计维度")
    private String statDim;
    @Schema(description = "统计对象")
    private List<StatObj> statObjs;

    @Data
    public static class StatObj {
        @Schema(description = "统计名称")
        private String statName;
        @Schema(description = "统计单位")
        private String statUnit;
        @Schema(description = "统计项")
        private List<StatItem> statItems;
    }

    @Data
    public static class StatItem {
        @Schema(description = "统计项名称")
        private String itemName;
        @Schema(description = "统计项值称")
        private BigDecimal itemVal;
    }

}