package com.kge.energy.crm.repository.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 资源表(BResource)实体类
 *
 * @author wangjihua
 * @since 2024-07-03 20:38:24
 */
@Data
@Accessors(chain = true)
public class BResource {

    @TableId(type = IdType.AUTO)
    private Integer resourceId; 

    /**
     * 父资源
     */
    private Integer parentResourceId; 

    /**
     * 停用
     */
    private Integer referResourceId; 

    /**
     * 0，1，2，3,路由三级
     */
    private Integer level; 

    /**
     * 子系统、板块、模块、功能块
     */
    private String type; 

    /**
     * 与真实路由对应
     */
    private String name; 

    /**
     * 管理后台资源名称
     */
    private String displayName; 

    /**
     * 排序
     */
    private Integer sort; 

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

