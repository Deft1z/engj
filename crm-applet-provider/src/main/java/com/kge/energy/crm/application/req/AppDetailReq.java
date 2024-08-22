package com.kge.energy.crm.application.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(name = "APP详情请求参数", description = "APP详情请求参数")
public class AppDetailReq {

    @Schema(description = "应用ID集合", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty
    private Set<Integer> appIds;
}
