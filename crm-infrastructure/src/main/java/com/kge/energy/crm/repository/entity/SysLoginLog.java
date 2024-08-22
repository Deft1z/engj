package com.kge.energy.crm.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class SysLoginLog {

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
     * 登录人ID
     */
    private Integer userId;

    /**
     * 登录人账号
     */
    private String userName;

    private String userRealname;

    private String userMobile;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;

    /**
     * 登录平台
     */
    private Integer loginPlatform;

    /**
     * 登录结果
     */
    private Integer loginResult;

    /**
     * 登录信息
     */
    private String loginMessage;
}
