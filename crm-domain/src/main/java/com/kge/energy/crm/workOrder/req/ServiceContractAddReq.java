package com.kge.energy.crm.workOrder.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
@Schema(description = "服务合同新增参数")
public class ServiceContractAddReq {

    @Schema(description = "工单id")
    private Integer formId;

    @Schema(description = "合同名称")
    private String name;

    @Schema(description = "公司")
    private String company;

    @NotBlank
    @Schema(description = "合同编号")
    private String code;

    @Schema(description = "合同金额")
    private Double amount;

    @Schema(description = "项目编号")
    private String projectCode;

    @Schema(description = "项目开始时间")
    private String projectStartTime;

    @Schema(description = "项目结束时间")
    private String projectEndTime;

    @Schema(description = "合同签订时间")
    private LocalDate signingTime;

    @Schema(description = "服务开始时间")
    private String serviceStartTime;

    @Schema(description = "服务结束时间")
    private String serviceEndTime;

    @Schema(description = "服务单位id")
    private Integer serviceUnit;

    @Schema(description = "工单详细信息json")
    private String content;

    @Schema(description = "发起工单人id")
    private Integer owner;

    @Schema(description = "负责人id")
    private Integer pm;

    @Schema(description = "备注")
    private String remark;

}
