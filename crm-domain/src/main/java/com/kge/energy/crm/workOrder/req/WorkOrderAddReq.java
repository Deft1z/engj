package com.kge.energy.crm.workOrder.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "业务工单新增参数")
public class WorkOrderAddReq {

    @Schema(description = "提交时间")
    private String timeSubmit;

    @Schema(description = "备注")
    private String remark;

    @Valid
    @NotNull
    @Schema(description = "工单内容")
    private WorkOrderContent content;

    @Data
    @Accessors(chain = true)
    public static class WorkOrderContent {
        @Schema(description = "工单编码")
        private String code;
        @NotBlank
        @Schema(description = "业务名称")
        private String businessName;
        @Schema(description = "客户姓名")
        private String customerName;
        @NotBlank
        @Schema(description = "手机号")
        private String mobile;
        @Schema(description = "所在地区")
        private String area;
        @Schema(description = "详细地址")
        private String detailedAddress;
        @Schema(description = "公司名称")
        private String companyName;
        @Schema(description = "电压等级")
        private String voltageLevel;
        @Schema(description = "用电容量")
        private String electricityCapacity;
        @Schema(description = "工单服务类型id")
        private String formTypeId;
    }

}
