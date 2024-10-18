package com.kge.energy.crm.complain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@Schema(description = "投诉新增参数")
public class ComplainAddReq {

    @NotNull
    @Schema(description = "服务合同或业务工单id")
    private Integer formId;
    @Schema(description = "1 服务合同投诉 2业务工单投诉")
    private Integer typef;
    @Schema(description = "投诉主题")
    private String subject;
    @Schema(description = "投诉内容")
    private String content;
    @Schema(description = "投诉公司")
    private String company;
    @Schema(description = "投诉公司id")
    private Integer organizationId;
    @Schema(description = "附件id")
    private List<Integer> fileIds;
    
}