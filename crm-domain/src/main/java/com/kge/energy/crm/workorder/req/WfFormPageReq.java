package com.kge.energy.crm.workorder.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kge.energy.crm.common.page.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "业务工单查询参数")
public class WfFormPageReq extends PageReq {

    @NotNull
    @Schema(description = "工单类型id, 1 业务工单 2 投诉工单")
    private Integer formTypeId;

    @Schema(description = "查询参数map")
    private SearchFormMap searchMap;

    @Schema(description = "租户id")
    private Integer tenantId;

    @Data
    @Accessors(chain = true)
    public static class SearchFormMap {
        @Schema(description = "状态")
        private String status;

        @Schema(description = "名称")
        private String name;

        @Schema(description = "查询开始时间")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime starttime;

        @Schema(description = "查询结束时间")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime endtime;

        @Schema(description = "只查userId")
        private String onlyMe;

        @Schema(description = "业务名称")
        private String businessName;

        @Schema(description = "服务单位id")
        private Integer serviceUnitId;
    }

}
