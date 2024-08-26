package com.kge.energy.crm.repository.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 接入应用(BApp)实体类
 *
 * @author wangjihua
 * @since 2024-07-03 20:38:24
 */
@Data
@Accessors(chain = true)
public class BApp {

    /**
     * 外部应用
     */
    @TableId(type = IdType.AUTO)
    private Integer appId;

    /**
     * 应用名称
     */
    private String name;

    /**
     * 与第三方系统约定好的公钥
     */
    private String appUuid;

    /**
     * 与第三方系统约定好的私钥
     */
    private String appSecret;

    /**
     * 应用首页
     */
    private String appAddress;

    /**
     * 业务系统后端接口访问地址
     */
    private String interfaceAddress;

    /**
     * 0 手机验证码；1 账号密码
     */
    private Integer bindType;

    /**
     * 用于账号密码绑定的网页地址
     */
    private String bindAddress;

    @TableField("is_commonly_used")
    private Boolean commonlyUsed;

    /**
     * 默认都是all
     */
    private String scope;

    /**
     * 软删除标识
     */
    private Integer flag;

    /**
     * 创建用户ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer createUserId;

    /**
     * 创建时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime;

    /**
     * 修改用户ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer modifyUserId;

    /**
     * 修改时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime modifyTime;

    /**
     * 备注
     */
    private String remark;
}

