package com.kge.energy.crm.msg.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@Schema(name = "删除消息模板配置", description = "删除消息模板配置对象")
public class DeleteSysMsgTemplateReq {

    @Schema(description = "消息模板配置ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer id;


}
