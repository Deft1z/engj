package com.kge.energy.crm.external.ct.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "外部系统远程 API 响应")
public class CtRemoteResp<T> {

    @Schema(description = "响应编码")
    private String ret;

    @Schema(description = "响应消息")
    private String msg;

    @Schema(description = "状态值")
    private Integer status;

    @Schema(description = "业务数据")
    private T data;

}
