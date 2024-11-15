package com.kge.energy.crm.msg.req;

import com.kge.energy.crm.common.page.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(name = "获取业务消息模板配置列表请求", description = "获取业务消息模板配置列表请求对象")
public class SysMsgTemplateListReq extends PageReq {

    @Schema(description = "业务功能配置ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer bizFunctionId;

}
