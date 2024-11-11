package com.kge.energy.crm.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * iam数据同步日志(IamSyncLog)实体类
 *
 * @author zhengwenke
 * @since 2024-11-11 10:20:38
 */
@TableName(value = "iam_sync_log")
@Data
@Builder
@Accessors(chain = true)
public class IamSyncLog {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    @TableField(value = "id")
    private Long id;

    /**
     * 同步时间
     */
    @TableField(value = "sync_time")
    private Object syncTime;

    /**
     * 同步数据名称
     */
    @TableField(value = "sync_name")
    private String syncName;

    /**
     * 同步内容
     */
    @TableField(value = "sync_content")
    private String syncContent;

    /**
     * 同步结果
     */
    @TableField(value = "sync_result")
    private String syncResult;

    /**
     * 成功标志
     */
    @TableField(value = "success_flag")
    private Boolean successFlag;

}



