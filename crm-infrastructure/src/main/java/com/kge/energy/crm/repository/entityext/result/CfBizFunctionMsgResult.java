package com.kge.energy.crm.repository.entityext.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 业务功能消息配置表(CfBizFunctionMsgConfig)响应对象
 *
 * @author zhengwenke
 * @since 2024-09-19 10:30:28
 */
@Data
@Accessors(chain = true)
@Schema(description = "业务功能消息配置表响应对象")
public class CfBizFunctionMsgResult {

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

    @Schema(description = "创建人ID")
    private Integer createUserId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人ID")
    private Integer modifyUserId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private LocalDateTime modifyTime;

    @Schema(description = "数据状态：-1-删除，1-正常")
    private Integer flag;

    @Schema(description = "租户id")
    private Integer tenantId;

}



