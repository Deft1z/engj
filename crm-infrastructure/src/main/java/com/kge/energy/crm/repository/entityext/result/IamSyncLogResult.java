package com.kge.energy.crm.repository.entityext.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * iam数据同步日志(IamSyncLog)响应对象
 *
 * @author zhengwenke
 * @since 2024-11-11 10:20:38
 */
@Data
@Accessors(chain = true)
@Schema(description = "iam数据同步日志响应对象")
public class IamSyncLogResult {

    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "同步时间")
    private Object syncTime;

    @Schema(description = "同步数据名称")
    private String syncName;

    @Schema(description = "同步内容")
    private String syncContent;

    @Schema(description = "同步结果")
    private String syncResult;

    @Schema(description = "成功标志")
    private Boolean successFlag;

}



