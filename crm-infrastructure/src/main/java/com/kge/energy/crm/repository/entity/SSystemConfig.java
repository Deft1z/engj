package com.kge.energy.crm.repository.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 系统配置(SSystemConfig)实体类
 *
 * @author wangjihua
 * @since 2024-07-03 20:38:24
 */
@Data
@Accessors(chain = true)
public class SSystemConfig {

    /**
     * 系统配置表（停用）
     */
    @TableId(type = IdType.AUTO)
    private Integer systemConfigId; 

    /**
     * 配置项
            price_basefee_by_demand: 基础电费按需单价
            price_basefee_by_capacity: 基础电费按容单价
     */
    private String name; 

    private String config; 

    private Integer flag; 

    @TableField(fill = FieldFill.INSERT)
    private Integer createUserId; 

    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime; 

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer modifyUserId; 

    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime modifyTime; 

    private String remark; 
}

