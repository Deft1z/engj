package com.kge.energy.crm.repository.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 数据权限配置表(CfDataPermission)实体类
 *
 * @author wangjihua
 * @since 2024-09-20 14:52:59
 */
@Data
@Accessors(chain = true)
public class CfDataPermission {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 业务功能配置ID
     */
    private Integer bizFunctionId;

    /**
     * 角色ID
     */
    private Integer roleId;

    /**
     * 数据权限范围类型：0-所有，1-租户，2-集团，3-公司，4-部门，5-本人
     */
    private Integer dataRangeType;

    /**
     * 优先级，越大越高
     */
    private Integer priority;

    /**
     * 数据状态：-1-删除，1-正常
     */
    private Integer flag;

    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer createUserId;

    /**
     * 创建时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime;

    /**
     * 更新人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer modifyUserId;

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

