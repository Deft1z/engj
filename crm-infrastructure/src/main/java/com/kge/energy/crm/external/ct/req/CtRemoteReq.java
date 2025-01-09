package com.kge.energy.crm.external.ct.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "外部系统远程 API 请求参数")
public class CtRemoteReq {

    @Schema(description = "应用ID")
    private String appID;

    @Schema(description = "业务参数")
    private DataReq data;

    @Schema(description = "时间戳")
    private String timeStamp;

    @Schema(description = "签名")
    private String sig;

    @Data
    @Accessors(chain = true)
    public static class DataReq {
        @Schema(description = "该用户用于第三方系统的ID")
        private String openID;

        @Schema(description = "业务范围，默认为“all”")
        private String scope = "all";
    }

}
