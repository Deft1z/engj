package com.kge.energy.crm.repository.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 业务功能配置表(CfBizFunction)实体类
 *
 * @author wangjihua
 * @since 2024-09-20 14:52:59
 */
@Data
@Accessors(chain = true)
public class CfBizFunction {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 业务模块名称
     */
    private String moduleName;

    /**
     * 业务模块编码
     */
    private String moduleCode;

    /**
     * 业务功能名称
     */
    private String functionName;

    /**
     * 业务功能编码
     */
    private String functionCode;

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

