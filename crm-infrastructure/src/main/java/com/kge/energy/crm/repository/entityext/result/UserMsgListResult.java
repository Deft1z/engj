package com.kge.energy.crm.repository.entityext.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kge.platform.framework.web.util.JsonUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author wangjihua
 */
@Data
@Accessors(chain = true)
@Schema(name = "用户消息列表响应参数", description = "用户消息列表响应参数")
public class UserMsgListResult {

    @Schema(description = "用户消息主键")
    private Long id;

    @Schema(description = "用户Id")
    private Integer userId;

    @Schema(description = "用户姓名")
    private String realname;

    @Schema(description = "消息业务id")
    private String msgBizId;

    @Schema(description = "消息业务类型， 0 告警信息 1 工单通知 2 项目合同 3 投诉处理")
    private Integer msgBizType;

    @Schema(description = "地址类型：system、file、bapp、link")
    private String pathType;

    @Schema(description = "消息内容Map，注意告警信息、工单通知、项目合同、投诉处理的消息内容Map字段均有差异，需按实际取值展示")
    private Map<String, Object> content;

    @Schema(description = "已读标识，false 未读 true 已读")
    private Boolean isRead;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "消息时间")
    private LocalDateTime createTime;

    public void setContent(String content) {
        this.content = JsonUtils.deserialize(content, Map.class);
    }


}
