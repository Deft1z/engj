package com.kge.energy.crm.msg.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 消息渠道列表(SysMsgChannel)响应对象
 *
 * @author zhengwenke
 * @since 2024-09-18 17:40:51
 */
@Data
@Accessors(chain = true)
@Schema(description = "消息渠道列表响应对象")
public class FunctionMsgChannelConfigResp {

    @Schema(description = "业务功能id")
    private Integer bizFunctionId;

    @Schema(description = "消息渠道id")
    private Integer msgChannelId;

    @Schema(description = "渠道编码")
    private String channelCode;

    @Schema(description = "渠道名称")
    private String channelName;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "黑名单,userIds")
    private String blacklist;

    @Schema(description = "白名单,userIds")
    private String whitelist;

    @Schema(description = "通知优先等级")
    private Integer priority;

    @Schema(description = "是否启用")
    private Boolean enabled;

}



