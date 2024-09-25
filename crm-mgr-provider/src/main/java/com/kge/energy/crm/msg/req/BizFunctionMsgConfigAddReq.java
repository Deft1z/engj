package com.kge.energy.crm.msg.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 业务功能消息配置(CfBizFunctionMsgConfig)新增对象
 *
 * @author zhengwenke
 * @since 2024-09-19 10:30:28
 */
@Data
@Accessors(chain = true)
@Schema(description = "业务功能消息配置新增对象")
public class BizFunctionMsgConfigAddReq {

    @NotNull
    @Schema(description = "业务功能id")
    private Integer bizFunctionId;

    @Schema(description = "消息配置")
    private List<MsgConfigAddReq> msgConfigs;

    @Data
    public static class MsgConfigAddReq {
        @NotNull
        @Schema(description = "消息渠道id")
        private Integer msgChannelId;

        @Schema(description = "黑名单,userIds")
        private List<Integer> blacklist;

        @Schema(description = "白名单,userIds")
        private List<Integer> whitelist;

        @Schema(description = "通知优先等级")
        private Integer priority;

        @Schema(description = "是否启用")
        private Boolean enabled = false;
    }

}



