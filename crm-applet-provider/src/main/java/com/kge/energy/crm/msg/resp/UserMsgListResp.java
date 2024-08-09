package com.kge.energy.crm.msg.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(name = "用户消息列表响应参数", description = "用户消息列表响应参数")
public class UserMsgListResp {

    @Schema(description = "用户消息主键")
    private Long id;

    @Schema(description = "消息业务id")
    private String msgBizId;

    @Schema(description = "地址类型：system、file、bapp、link")
    private String pathType;

    @Schema(description = "消息内容")
    private String content;

    @Schema(description = "已读标识，false 未读 true 已读")
    private Boolean read;
}
