package com.kge.energy.crm.repository.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


@TableName(value = "sys_msg_template")
@Data
@Accessors(chain = true)
public class SysMsgTemplate {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 业务功能id
     */
    private Integer bizFunctionId;

    /**
     * 消息模板编码
     */
    private String templateCode;

    /**
     * 消息模板内容
     */
    private String templateContent;

    /**
     * elink消息请求参数
     */
    private String elinkParams;

    /**
     * elink或短信消息请求参数
     */
    private String eSmsParams;

    /**
     * 玄武短信息消息请求参数
     */
    private String smsParams;

    /**
     * 微信小程序消息请求参数
     */
    private String appletParams;

    /**
     * 公众号消息请求参数，预留字段
     */
    private String offiaccountParams;

    /**
     * 邮件消息请求参数，预留字段
     */
    private String emailParams;


    /**
     * 是否启用，0-禁用，1-启用
     */
    private Integer isEnabled;


    /**
     * 消息目标，user 用户个人消息 role 角色多人消息
     */
    private String msgTarget;


    /**
     * 用途备注
     */
    private String remark;


    /**
     * 创建时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime modifyTime;

    /**
     * 租户id
     */
    private Integer tenantId;

}


