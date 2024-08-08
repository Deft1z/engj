package com.kge.energy.crm.repository.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 角色资源权限(RRoleResource)实体类
 *
 * @author wangjihua
 * @since 2024-07-29 16:54:07
 */
@Data
@Accessors(chain = true)
public class RRoleResource {

    /**
     * 角色资源表
     */
    @TableId(type = IdType.AUTO)
    private Integer roleResourceId;

    /**
     * b_role主键
     */
    private Integer roleId;

    /**
     * b_resource主键
     */
    private Integer resourceId;

    /**
     * 数据状态：-1-删除，1-正常
     */
    private Integer flag;

    /**
     * 读权限，重构后停用
     */
    private Integer authRead;

    /**
     * 写权限，重构后停用
     */
    private Integer authWrite;

    /**
     * 删除权限，重构后停用
     */
    private Integer authDelete;

    /**
     * 审核权限，重构后停用
     */
    private Integer authAudit;

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

    /**
     * 租户id
     */
    private Integer tenantId;
}

