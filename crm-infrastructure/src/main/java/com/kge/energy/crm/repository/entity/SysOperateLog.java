package com.kge.energy.crm.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 操作日志记录(SysOperateLog)实体类
 *
 * @author wangjihua
 * @since 2024-07-29 17:34:33
 */
@Data
@Accessors(chain = true)
public class SysOperateLog {

    /**
     * 日志主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 链路追踪编号
     */
    private String traceId;

    /**
     * 租户ID
     */
    private Integer tenantId;

    /**
     * 租户名
     */
    private String tenantName;

    /**
     * 操作人ID
     */
    private Integer operateId;

    /**
     * 操作人名称
     */
    private String operateName;

    /**
     * 操作时间
     */
    private LocalDateTime operateTime;

    /**
     * 操作模块
     */
    private Integer operateModule;

    /**
     * 操作行为
     */
    private String operateBehavior;
}

