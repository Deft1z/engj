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
public class SysMsgChannelResp {

    @Schema(description = "主键id")
    private Integer id;

    @Schema(description = "渠道编码")
    private String channelCode;

    @Schema(description = "渠道名称")
    private String channelName;

    @Schema(description = "备注")
    private String remark;

}



