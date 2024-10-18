package com.kge.energy.crm.workflow.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "工单服务类型")
public class WfFormTypeTreeResp {

    @Schema(description = "服务id")
    private Integer serviceId;

    @Schema(description = "服务名称")
    private String serviceName;

    @Schema(description = "子服务")
    private List<WfFormTypeTreeResp> children;

}